package com.illiarb.tmdbclient.libs.ui.common

import android.view.View
import com.google.android.material.snackbar.Snackbar

class SnackbarController {

  private var snackbar: Snackbar? = null

  fun showMessage(root: View, message: String) {
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