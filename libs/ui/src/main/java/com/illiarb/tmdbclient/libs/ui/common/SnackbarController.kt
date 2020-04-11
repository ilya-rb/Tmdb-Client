package com.illiarb.tmdbclient.libs.ui.common

import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class SnackbarController {

  private var snackbar: Snackbar? = null

  suspend fun bind(root: View, messages: Flow<String?>) {
    messages.collect { message ->
      if (message == null) {
        snackbar?.dismiss()
        snackbar = null
      } else {
        if (snackbar == null) {
          snackbar = Snackbar.make(root, message, Snackbar.LENGTH_INDEFINITE)
        } else {
          snackbar!!.setText(message)
        }

        if (!snackbar!!.isShown) {
          snackbar!!.show()
        }
      }
    }
  }
}