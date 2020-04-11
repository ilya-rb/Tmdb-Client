package com.illiarb.tmdbclient.libs.ui.details.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.libs.ui.ext.dimen

class CurvedImageView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

  private val curvePath = Path()
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private var curveOffset: Float

  init {
    var attributes: TypedArray? = null
    try {
      attributes = context.obtainStyledAttributes(attrs, R.styleable.CurvedImageView)

      curveOffset = attributes.getInt(
        R.styleable.CurvedImageView_cvCurveOffset,
        dimen(R.dimen.movie_details_curve_offset)
      ).toFloat()

    } finally {
      attributes?.recycle()
    }
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    curvePath.apply {
      reset()

      val height = measuredHeight.toFloat()
      val width = measuredWidth.toFloat()

      // top left to bottom left
      lineTo(0f, height - curveOffset)
      // arc from bottom left to bottom right
      quadTo(
        width / 2,
        height + curveOffset,
        width,
        height - curveOffset
      )
      // bottom right to top right
      lineTo(width, 0f)
      // top right to top left
      close()
    }
  }

  override fun onDraw(canvas: Canvas) {
    canvas.drawPath(curvePath, paint)
    canvas.clipPath(curvePath)
    super.onDraw(canvas)
  }

  /**
   * Used by MotionLayout as custom attribute
   */
  @Suppress("unused")
  fun setCurveOffset(offset: Int) {
    curveOffset = offset.toFloat()
    requestLayout()
  }
}