package com.file.fileutils

import java.lang.Exception


 interface Callback{
    fun onSuccess(data:ArrayList<String>)
    fun onSuccess(isSuccess:Boolean)
    fun onFailure(ex: Exception)
}


interface FileGroupedCallback{
     fun onSuccess(data: HashMap<String, ArrayList<String>?>)

     fun onFailure(ex: Exception)
}

interface SpecificFolderFileCallback{
    fun onSuccess(data: ArrayList<String?>)

    fun onFailure(ex: Exception)
}

interface GetStorageStateCallBack{
     fun onSuccess(total:Long , available:Long)

     fun onFailure(ex: Exception)
}
