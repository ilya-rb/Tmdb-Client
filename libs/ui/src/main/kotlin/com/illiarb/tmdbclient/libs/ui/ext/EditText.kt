package com.illiarb.tmdbclient.libs.ui.ext

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

fun EditText.textChanges() = callbackFlow<String> {
  val watcher = object : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
    override fun afterTextChanged(s: Editable?) {
      if (!s.isNullOrEmpty()) {
        offer(s.toString())
      }
    }
  }

  addTextChangedListener(watcher)

  awaitClose {
    removeTextChangedListener(watcher)
  }
}