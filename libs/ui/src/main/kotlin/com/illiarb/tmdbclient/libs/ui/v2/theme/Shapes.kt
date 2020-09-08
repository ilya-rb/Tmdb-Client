package com.illiarb.tmdbclient.libs.ui.v2.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp

@Suppress("MagicNumber")
internal val TmdbShapes: Shapes
  get() = Shapes(
    small = RoundedCornerShape(percent = 50),
    medium = RoundedCornerShape(size = 8.dp),
    large = RoundedCornerShape(size = 0.dp)
  )