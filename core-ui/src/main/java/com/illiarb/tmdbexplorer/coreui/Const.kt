package com.illiarb.tmdbexplorer.coreui

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

/**
 * @author ilya-rb on 26.10.18.
 */
object Const {

    const val PARSE_DATE_FORMAT = "yyyy-mm-dd"
    const val DISPLAY_DATE_FORMAT = "dd MMMM"

    @SuppressLint("SimpleDateFormat")
    val PARSE_DATE_SDF = SimpleDateFormat(PARSE_DATE_FORMAT)

    @SuppressLint("SimpleDateFormat")
    val DISPLAY_DATE_SDF = SimpleDateFormat(DISPLAY_DATE_FORMAT)
}