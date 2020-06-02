package com.illiarb.tmdbclient.libs.ui.ext

import android.graphics.drawable.Drawable

fun Drawable.getTintedDrawable(color: Int): Drawable {
  return mutate().also {
    it.setTint(color)
  }
}