package com.illiarb.tmdbclient.libs.ui.v2.theme

import android.content.Context
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun TmdbTheme(
  context: Context,
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colors = TmdbColors(context),
    typography = TmdbTypography,
    shapes = TmdbShapes,
    content = content,
  )
}