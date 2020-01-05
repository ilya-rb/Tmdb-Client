package com.illiarb.tmdbexplorer.coreui.ext

import android.widget.TextView
import com.illiarb.tmdbexplorer.coreui.common.Text

fun TextView.setText(text: Text) {
    when (text) {
        is Text.AsString -> this.text = text.text
        is Text.AsResource -> this.text = resources.getString(text.id)
    }
}