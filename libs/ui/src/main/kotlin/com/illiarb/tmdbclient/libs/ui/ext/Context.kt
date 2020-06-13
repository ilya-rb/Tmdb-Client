package com.illiarb.tmdbclient.libs.ui.ext

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun Context.getTintedDrawable(@DrawableRes drawableRes: Int, color: Int): Drawable {
  val drawable = ContextCompat.getDrawable(this, drawableRes)!!

  return drawable.mutate().also {
    it.setTint(color)
  }
}