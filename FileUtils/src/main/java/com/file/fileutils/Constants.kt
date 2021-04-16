package com.file.fileutils

enum class UriType {
    VIDEO_URL,
  AUDIO_URL,
 IMAGE_URL
}

enum class FileExtension(var exter:String) {
    PDF(".pdf"),
    DOC(".doc"),
    MP4(".mp4"),
    PNG(".png"),
    MP3(".mp3"),
    APK(".apk")
}