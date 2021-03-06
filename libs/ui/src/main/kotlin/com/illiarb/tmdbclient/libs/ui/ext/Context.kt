package com.illiarb.tmdbclient.libs.ui.ext

import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat

fun Context.getTintedDrawable(@DrawableRes drawableRes: Int, color: Int): Drawable {
  val drawable = ContextCompat.getDrawable(this, drawableRes)!!

  return drawable.mutate().also {
    it.setTint(color)
  }
}

fun Context.isNightModeEnabled(): Boolean {
  return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}

fun Context.getColorAttr(@AttrRes id: Int): Int {
  val outValue = TypedValue()
  theme.resolveAttribute(id, outValue, true)
  return outValue.data
}