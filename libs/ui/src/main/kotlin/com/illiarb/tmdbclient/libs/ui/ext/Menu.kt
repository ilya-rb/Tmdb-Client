package com.illiarb.tmdbclient.libs.ui.ext

import android.view.Menu
import androidx.core.view.forEach

fun Menu.tintMenuItemsWithColor(color: Int) {
  forEach {
    it.icon?.let { icon ->
      it.icon = icon.mutate().apply {
        setTint(color)
      }
    }
  }
}