package com.illiarb.tmdbclient.modules.details.v2

import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density

class MoviePosterShape(private val offset: Float) : Shape {

  override fun createOutline(size: androidx.compose.ui.geometry.Size, density: Density): Outline {
    return Outline.Generic(Path().apply {
      reset()

      val height = size.height
      val width = size.width

      // top left to bottom left
      lineTo(0f, height - offset)
      // arc from bottom left to bottom right
      quadraticBezierTo(
        width / 2,
        height + offset,
        width,
        height - offset
      )
      // bottom right to top right
      lineTo(width, 0f)
      // top right to top left
      close()
    })
  }

  override fun toString(): String = "MoviePosterShape(offset = $offset"
}