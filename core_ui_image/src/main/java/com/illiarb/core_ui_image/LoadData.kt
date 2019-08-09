package com.illiarb.core_ui_image

/**
 * @author ilya-rb on 29.12.18.
 */
sealed class LoadData {

    data class Url(val path: String?) : LoadData()

    data class File(val file: java.io.File) : LoadData()

    data class Uri(val uri: android.net.Uri) : LoadData()
}