package com.illiarb.tmdbclient.ui

import android.animation.ArgbEvaluator
import android.animation.FloatEvaluator
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Color as AndroidColor

class ToolbarScrollListener(
  private val startColor: Int = AndroidColor.TRANSPARENT,
  private val endColor: Int,
  private val endElevation: Float,
  private val distance: Int,
  private val onUpdate: (Elevation, Color) -> Unit
) : RecyclerView.OnScrollListener() {

  private val colorEvaluator = ArgbEvaluator()
  private val elevationEvaluator = FloatEvaluator()

  override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
    super.onScrolled(recyclerView, dx, dy)

    val fraction = calculateFraction(recyclerView.computeVerticalScrollOffset(), distance)
    val color = colorEvaluator.evaluate(fraction, startColor, endColor) as Int
    val elevation = elevationEvaluator.evaluate(fraction, 0, endElevation)

    onUpdate(Elevation(elevation), Color(color))
  }

  private fun Int.toPercentOf(max: Int): Int = this * 100 / max

  private fun calculateFraction(start: Int, end: Int): Float {
    return start.coerceAtMost(end).toPercentOf(end) / 100f
  }
}

inline class Elevation(val value: Float)

inline class Color(val value: Int)