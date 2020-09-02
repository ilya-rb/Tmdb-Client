package com.illiarb.tmdbclient.libs.ui.v2.theme

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.illiarb.tmdbclient.libs.ui.R
import com.illiarb.tmdbclient.libs.ui.ext.getColorAttr

@Composable
internal fun TmdbColors(context: Context): Colors {
  val colorPrimary = context.getColorAttr(R.attr.colorPrimary)
  val colorOnPrimary = context.getColorAttr(R.attr.colorOnPrimary)
  val colorPrimaryVariant = context.getColorAttr(R.attr.colorPrimaryVariant)
  val colorSecondary = context.getColorAttr(R.attr.colorSecondary)
  val colorOnSecondary = context.getColorAttr(R.attr.colorOnSecondary)
  val colorSecondaryVariant = context.getColorAttr(R.attr.colorSecondaryVariant)
  val colorSurface = context.getColorAttr(R.attr.colorSurface)
  val colorOnSurface = context.getColorAttr(R.attr.colorOnSurface)
  val colorError = context.getColorAttr(R.attr.colorError)
  val colorOnError = context.getColorAttr(R.attr.colorOnError)
  val colorOnBackground = context.getColorAttr(R.attr.colorOnBackground)
  val colorBackground = context.getColorAttr(android.R.attr.colorBackground)

  return Colors(
    primary = Color(colorPrimary),
    onPrimary = Color(colorOnPrimary),
    primaryVariant = Color(colorPrimaryVariant),
    secondary = Color(colorSecondary),
    onSecondary = Color(colorOnSecondary),
    secondaryVariant = Color(colorSecondaryVariant),
    surface = Color(colorSurface),
    onSurface = Color(colorOnSurface),
    error = Color(colorError),
    onError = Color(colorOnError),
    onBackground = Color(colorOnBackground),
    background = Color(colorBackground),
    isLight = !isSystemInDarkTheme()
  )
}