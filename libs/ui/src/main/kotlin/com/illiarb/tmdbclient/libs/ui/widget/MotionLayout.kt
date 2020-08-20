package com.illiarb.tmdbclient.libs.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout as AndroidXMotionLayout

/**
 * Motion layout that allows to skip touch
 * events when on different states
 */
class MotionLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : AndroidXMotionLayout(context, attrs, defStyleAttr) {

  private val skipTouchEventsOnStateRegistry = mutableMapOf<Int, SkipTouchEventEntry>()
  private val childViews = mutableSetOf<View>()

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)

    for (i in 0 until childCount) {
      childViews.add(getChildAt(i))
    }
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    val entry = skipTouchEventsOnStateRegistry[currentState]
    if (entry?.skip == true) {
      for (i in 0 until childCount) {
        val view = getChildAt(i)
        val viewRect = Rect().also { view.getHitRect(it) }

        if (viewRect.contains(event.x.toInt(), event.y.toInt()) &&
          entry.excludeViewIds.contains(view.id)
        ) {
          return super.onTouchEvent(event)
        }
      }
      return false
    }
    return super.onTouchEvent(event)
  }

  fun setSkipTouchEventOnState(
    stateId: Int,
    skip: Boolean,
    excludeViewIds: List<Int> = emptyList()
  ) {
    skipTouchEventsOnStateRegistry[stateId] = SkipTouchEventEntry(skip, excludeViewIds)
  }

  private data class SkipTouchEventEntry(
    val skip: Boolean,
    val excludeViewIds: List<Int>
  )
}