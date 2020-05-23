package com.illiarb.tmdbclient.libs.imageloader

import androidx.annotation.DrawableRes

sealed class Image {

  data class Network(val url: String) : Image()

  data class File(val file: java.io.File) : Image()

  data class Resource(@DrawableRes val resId: Int) : Image()

  data class Uri(val uri: android.net.Uri) : Image()
}