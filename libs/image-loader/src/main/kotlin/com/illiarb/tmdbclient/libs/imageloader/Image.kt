package com.illiarb.tmdbclient.libs.imageloader

import androidx.annotation.DrawableRes
import android.net.Uri as AndroidUri
import java.io.File as JavaFile

sealed class Image {

  data class Network(val url: String) : Image()

  data class File(val file: JavaFile) : Image()

  data class Resource(@DrawableRes val resId: Int) : Image()

  data class Uri(val uri: AndroidUri) : Image()
}