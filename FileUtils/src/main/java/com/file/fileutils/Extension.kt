package com.file.fileutils

import java.io.File

object Extension {
    fun File.getFolderSize(): Long {
        if (this.exists()) {
            var result: Long = 0
            val fileList: Array<File>? = this.listFiles()
            for (i in fileList?.indices!!) {
                // Recursive call if it's a directory
                result += if (fileList.get(i).isDirectory == true) {
                    fileList[i].getFolderSize()
                } else {
                    // Sum the file size in bytes
                    fileList.get(i).length()
                }
            }
            return result // return the file size
        }
        return 0
    }
}