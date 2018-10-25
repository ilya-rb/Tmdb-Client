package com.illiarb.tmdbexplorer.coreui.ext

import android.widget.TextView
import com.illiarb.tmdbexplorer.coreui.Const

/**
 * @author ilya-rb on 26.10.18.
 */
fun TextView.setDateText(dateString: String) {
    val date = Const.PARSE_DATE_SDF.parse(dateString)
    text = Const.DISPLAY_DATE_SDF.format(date)
}