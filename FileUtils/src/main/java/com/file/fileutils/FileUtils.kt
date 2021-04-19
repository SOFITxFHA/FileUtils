package com.file.fileutils

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.MediaStore
import android.util.Log
import com.file.fileutils.Extension.getFolderSize
import kotlinx.coroutines.*
import java.io.File
import java.lang.NullPointerException


object FileUtils {


    val listOfApk: ArrayList<String> = ArrayList()
    val listOfFilesFromFolder: ArrayList<String> = ArrayList()
    val listOfEmptyFolders: ArrayList<String> = ArrayList()
    val listOfCache: ArrayList<String> = ArrayList()
    val listOfGroupedFiles: HashMap<String, ArrayList<String>?> = HashMap()

// Get Files
    fun getFiles(context: Context, type: UriType, callback: Callback) {

        CoroutineScope(Dispatchers.Default).launch {
            try {


                val fileList: ArrayList<String> = ArrayList()
                val collection: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                    when (type) {
                        UriType.VIDEO_URL -> {
                            MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                        }
                        UriType.AUDIO_URL -> {
                            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                        }
                        UriType.IMAGE_URL -> {
                            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                        }
                    }


                } else {

                    when (type) {
                        UriType.VIDEO_URL -> {
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        }
                        UriType.AUDIO_URL -> {
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        }
                        UriType.IMAGE_URL -> {
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        }
                    }

                }
                val projection = arrayOf(
                    MediaStore.Video.Media._ID

                )

                val sortOrder = MediaStore.Video.Media.SIZE + " DESC"
                context.contentResolver.query(
                    collection,
                    projection,
                    null,
                    null,
                    sortOrder
                ).use { cursor ->
                    // Cache column indices.
                    val idColumn: Int =
                        cursor?.getColumnIndexOrThrow(MediaStore.Video.Media._ID) ?: 0
                    val uri = when (type) {
                        UriType.VIDEO_URL -> {
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        }
                        UriType.IMAGE_URL -> {
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        }
                        UriType.AUDIO_URL -> {
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        }
                    }
                    while (cursor?.moveToNext() == true) {
                        // Get values of columns for a given video.
                        val id: Long = cursor.getLong(idColumn)


                        val contentUri = ContentUris.withAppendedId(
                            uri, id
                        )

                        // Stores column values and the contentUri in a local object
                        // that represents the media file.

                        contentUri.toString()?.let { fileList.add(it) }
                    }
                }
                withContext(Dispatchers.Main) {
                    callback.onSuccess(fileList)
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    callback.onFailure(ex)
                }
            }

        }
    }


    //Get File By Extension
    fun getFilesByExtension(extension: FileExtension, rootDir: File, callback: Callback) {
        listOfApk.clear()
        CoroutineScope(Dispatchers.Default).launch {
            try {
                getFilesByExtension2(rootDir, extension)
                withContext(Dispatchers.Main) {
                    callback.onSuccess(listOfApk)
                }

            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    callback.onFailure(ex)
                }
            }

        }
    }
    private suspend fun getFilesByExtension2(rootDir: File, extension: FileExtension) {

        try {


            val pdfPattern = extension.exter
            val listFile = rootDir.listFiles()

            if (listFile != null) {
                for (i in listFile.indices) {
                    if (listFile[i].isDirectory) {
                        getFilesByExtension2(listFile[i], extension)
                    } else {

                        if (listFile[i].name.endsWith(pdfPattern)) {
                            listOfApk.add(listFile[i].absolutePath)


                        }
                    }
                }


            }

        } catch (ex: Exception) {
            throw ex
        }
    }


    //Get File From specific folder
     fun getFileFromFolder(rootDir: File, callback: Callback) {
         listOfFilesFromFolder.clear()
        CoroutineScope(Dispatchers.Default).launch {
            try {
                withContext(Dispatchers.Default){

                    getFileFromFolder2(rootDir)
                }

                withContext(Dispatchers.Main) {
                    callback.onSuccess(listOfFilesFromFolder)
                }

            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    callback.onFailure(ex)
                }
            }

        }
    }
    private  fun getFileFromFolder2(rootDir: File) {

        try {


            val listFile = rootDir.listFiles()

            if (listFile != null) {
                for (i in listFile.indices) {
                    if (listFile[i].isDirectory) {

                        getFileFromFolder2(listFile[i])
                    } else {


                        listOfFilesFromFolder.add(listFile[i].absolutePath)
                        Log.d(
                            "file ",
                            listOfFilesFromFolder.size.toString() + " " + listFile[i].path
                        )
                    }
                }


            } else {
                if (!rootDir.isDirectory) {
                    listOfFilesFromFolder.add(rootDir.absolutePath)
                }
            }

        } catch (ex: Exception) {
            throw ex
        }
    }

        //Delete File
    fun deleteFileRecursively(rootDir: File, callback: Callback) {
        var deleted=false

        CoroutineScope(Dispatchers.Default).launch {
            try {
                withContext(Dispatchers.Default){

                  deleted=rootDir.deleteRecursively()
                    listOfFilesFromFolder
                }

                withContext(Dispatchers.Main) {
                    if(deleted){
                        callback.onSuccess(true)
                    }
                    else{
                        callback.onSuccess(false)
                    }

                }

            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    callback.onFailure(ex)
                }
            }

        }
    }

    //Get Emprty Folder
    fun getEmptyFolders(callback: Callback){
        listOfEmptyFolders.clear()
        CoroutineScope(Dispatchers.Default).launch {
            try {
                getEmptyFolders2(Environment.getExternalStorageDirectory())


                withContext(Dispatchers.Main) {
                    callback.onSuccess(listOfEmptyFolders)
                }

            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    callback.onFailure(ex)
                }
            }

        }
    }
    private suspend fun getEmptyFolders2(dir: File) {

        // Populate an array with files/folders in the directory
        val folderContents = dir.listFiles()
        if (folderContents.isNullOrEmpty()) {
            // we are empty, add us.
            listOfEmptyFolders.add(dir.absolutePath)
            return
        }

        // Iterate through every file/folder
        for (content in folderContents) {
            // Disregard files, acquire folders
            if (content.isDirectory) {
                // check if this folder is empty

                if(content.getFolderSize()>0){

                    getEmptyFolders2(content)
                }
                else{
                    listOfEmptyFolders.add(content.absolutePath)
                }
            }
        }
    }


    //Get Application cache file
    fun getAppsCache(callback: Callback){
        listOfCache.clear()
        CoroutineScope(Dispatchers.Default).launch {
            try {
                getAppsCache2(Environment.getExternalStorageDirectory())


                withContext(Dispatchers.Main) {
                    callback.onSuccess(listOfCache)
                }

            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    callback.onFailure(ex)
                }
            }

        }
    }
    private suspend fun getAppsCache2(dir: File) {

        // Populate an array with files/folders in the directory

        val folderContents = dir.listFiles()

        if (folderContents.isNullOrEmpty() ) {
            // we are empty, add us.
            if(dir.name=="cache"){

                if(File(dir.path + "/cache").getFolderSize()>0){
                    listOfCache.add(dir.absolutePath + "/cache")
                }



            }
            return

        }

        // Iterate through every file/folder
        for (content in folderContents) {
            // Disregard files, acquire folders
            if (content.isDirectory && content.name=="cache") {
                if(File(dir.path + "/cache").getFolderSize()>0) {
                    listOfCache.add(dir.absolutePath + "/cache")
                }
            }
            else{
                // check if this folder is empty
                getAppsCache2(content)
            }
        }

    }


    fun getGroupedFolderFiles(rootDir: File, fileExtension: UriType, callback: FileGroupedCallback){
        listOfGroupedFiles.clear()
        CoroutineScope(Dispatchers.Default).launch {
            try {

                val listOfExternsion:ArrayList<String> = ArrayList()
                when(fileExtension){
                    UriType.VIDEO_URL -> {

                        listOfExternsion.add("mp4")

                    }
                    UriType.IMAGE_URL -> {
                        listOfExternsion.add("png")
                        listOfExternsion.add("jpeg")
                        listOfExternsion.add("jpg")
                        listOfExternsion.add("svg")
                    }
                    UriType.AUDIO_URL -> {
                        listOfExternsion.add("mp3")

                    }
                }
                getGroupedFolderFiles2(
                    Environment.getExternalStorageDirectory(),
                    fileExtension,
                    listOfExternsion
                )


                withContext(Dispatchers.Main) {
                    callback.onSuccess(listOfGroupedFiles)
                }

            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    callback.onFailure(ex)
                }
            }

        }


    }

    private suspend fun getGroupedFolderFiles2(
        rootDir: File,
        type: UriType,
        listOfExternsion: ArrayList<String>
    ) {

        try {


            val listFile = rootDir.listFiles()

            if (listFile != null) {
                for (i in listFile.indices) {
                    if (listFile[i].isDirectory) {
                        getGroupedFolderFiles2(listFile[i], type, listOfExternsion)
                    } else {


                        if(listOfExternsion.contains(listFile[i].extension))
                        {
                            if(listOfGroupedFiles.containsKey(File(rootDir.path).name)){
                                val listof=listOfGroupedFiles[File(rootDir.path).name]
                                listof?.add(listFile[i].path)
                                listOfGroupedFiles[File(rootDir.path).name] = listof
                            }
                            else{
                                val listof:ArrayList<String> = ArrayList()
                                listof.add(listFile[i].path)
                                listOfGroupedFiles[File(rootDir.path).name] = listof
                            }

                        }




                    }
                }


            }
            else{
                if(!rootDir.isDirectory){
                    if(listOfGroupedFiles.containsKey(File(rootDir.path).name)){
                        val listof=listOfGroupedFiles[File(rootDir.path).name]
                        listof?.add(rootDir.path)
                        listOfGroupedFiles[File(rootDir.path).name] = listof
                    }
                    else{
                        val listof:ArrayList<String> = ArrayList()
                        listof.add(rootDir.path)
                        listOfGroupedFiles[File(rootDir.path).name] = listof
                    }


                }
            }

        } catch (ex: Exception) {
            throw ex
        }
    }


     fun getAvailableExternalMemorySize(callBack: GetStorageStateCallBack) {
         CoroutineScope(Dispatchers.Default).launch {
             if (externalMemoryAvailable()) {
                 val path = Environment.getExternalStorageDirectory()
                 val stat = StatFs(path.path)
                 val blockSize = stat.blockSizeLong
                 val availableBlocks = stat.availableBlocksLong
                 val totalBlock = stat.blockCountLong
                 callBack.onSuccess(totalBlock * blockSize , availableBlocks * blockSize)

             }
             else{
                 callBack.onFailure(throw NullPointerException())
             }
         }

    }

    fun externalMemoryAvailable(): Boolean {
        return Environment.getExternalStorageState() ==
                Environment.MEDIA_MOUNTED
    }



}


