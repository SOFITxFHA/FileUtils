package com.friend.sample

import android.app.ActivityManager
import android.content.Context
import android.view.View
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


object ExtensionFun {
    var directBoost=false
    fun View.hide(){
        this.visibility=View.GONE
    }

    fun View.visible(){
        this.visibility=View.VISIBLE
    }

    fun View.inVisible(){
        this.visibility=View.INVISIBLE
    }




    fun Long.getFileSize():String{

            val n: Long = 1000
            var s = ""
            val kb = this / n.toDouble()
            val mb = kb / n
            val gb = mb / n
            val tb = gb / n
            if (this < n) {
                s = "$this Bytes"
            } else if (this >= n && this < n * n) {
                s = String.format("%.2f", kb) + " KB"
            } else if (this >= n * n && this < n * n * n) {
                s = String.format("%.2f", mb) + " MB"
            } else if (this >= n * n * n && this < n * n * n * n) {
                s = String.format("%.2f", gb) + " GB"
            } else if (this >= n * n * n * n) {
                s = String.format("%.2f", tb) + " TB"
            }
            return s

    }


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

    fun isAppRunning(context: Context, packageName: String): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val procInfos = activityManager.runningAppProcesses
        if (procInfos != null) {
            for (processInfo in procInfos) {
                if (processInfo.processName == packageName) {
                    return true
                }
            }
        }
        return false
    }


    fun getDateCurrentTimeZone(timestamp: Long): String? {
        try {
            val calendar: Calendar = Calendar.getInstance()
            val tz: TimeZone = TimeZone.getDefault()
            calendar.timeInMillis = timestamp
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.timeInMillis))
            val sdf = SimpleDateFormat("yyyy-MM-dd hh.mm aa")
            val currenTimeZone: Date = calendar.time as Date
            return sdf.format(currenTimeZone)
        } catch (e: Exception) {
        }
        return ""
    }



}