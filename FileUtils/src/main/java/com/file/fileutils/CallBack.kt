package com.file.fileutils

import java.lang.Exception


 interface Callback{
    public abstract fun onSuccess(data:ArrayList<String>)
    public abstract fun onSuccess(isSuccess:Boolean)
    public abstract fun onFailure(ex: Exception)
}


interface FileGroupedCallback{
    public abstract fun onSuccess(data: HashMap<String, ArrayList<String>?>)

    public abstract fun onFailure(ex: Exception)
}


