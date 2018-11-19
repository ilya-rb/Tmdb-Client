package com.illiarb.tmdbexplorer.coreui

import java.text.SimpleDateFormat
import java.util.Locale

/**
 * @author ilya-rb on 26.10.18.
 */
object Const {

    const val PARSE_DATE_FORMAT = "yyyy-mm-dd"
    const val DISPLAY_DATE_FORMAT = "dd MMMM"

    val PARSE_DATE_SDF: SimpleDateFormat
        get() = SimpleDateFormat(PARSE_DATE_FORMAT, Locale.getDefault())

    val DISPLAY_DATE_SDF: SimpleDateFormat
        get() = SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.getDefault())
}