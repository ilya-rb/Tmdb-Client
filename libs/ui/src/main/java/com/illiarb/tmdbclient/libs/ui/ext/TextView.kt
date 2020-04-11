package com.illiarb.tmdbclient.libs.ui.ext

import android.widget.TextView
import com.illiarb.tmdbclient.libs.ui.common.Text

fun TextView.setText(text: Text) {
  when (text) {
    is Text.AsString -> this.text = text.text
    is Text.AsResource -> this.text = resources.getString(text.id)
  }
}