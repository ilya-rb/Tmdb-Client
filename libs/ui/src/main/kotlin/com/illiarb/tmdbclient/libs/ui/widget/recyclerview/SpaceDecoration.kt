package com.illiarb.tmdbclient.libs.ui.widget.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author ilya-rb on 18.01.19.
 */
class SpaceDecoration(
  private val spacingLeft: Int = 0,
  private val spacingRight: Int = 0,
  private val spacingTop: Int = 0,
  private val spacingBottom: Int = 0,
  private val spacingLeftFirst: Int = spacingLeft,
  private val spacingRightLast: Int = spacingRight,
  private val spacingTopFirst: Int = spacingTop,
  private val spacingBottomLast: Int = spacingBottom,
  private val orientation: Int = LinearLayoutManager.VERTICAL
) : RecyclerView.ItemDecoration() {

  companion object {

    fun edgeInnerSpace(edgeSpace: Int, innerSpace: Int): SpaceDecoration {
      return SpaceDecoration(
        orientation = LinearLayoutManager.HORIZONTAL,
        spacingLeftFirst = edgeSpace,
        spacingLeft = innerSpace,
        spacingRight = innerSpace,
        spacingRightLast = edgeSpace
      )
    }

    fun edgeInnerSpaceVertical(edgeSpace: Int, innerSpace: Int): SpaceDecoration {
      return SpaceDecoration(
        orientation = LinearLayoutManager.VERTICAL,
        spacingTopFirst = edgeSpace,
        spacingTop = innerSpace,
        spacingBottom = innerSpace,
        spacingBottomLast = edgeSpace
      )
    }
  }

  override fun getItemOffsets(
    outRect: Rect,
    view: View,
    parent: RecyclerView,
    state: RecyclerView.State
  ) {
    outRect.setEmpty()
    outRect.left = spacingLeft
    outRect.right = spacingRight
    outRect.top = spacingTop
    outRect.bottom = spacingBottom

    if (orientation == LinearLayoutManager.VERTICAL) {
      if (isFirstItem(parent, view)) {
        outRect.top = spacingTopFirst
      }

      if (isLastItem(parent, view)) {
        outRect.bottom = spacingBottomLast
      }
    } else {
      if (isFirstItem(parent, view)) {
        outRect.left = spacingLeftFirst
      }

      if (isLastItem(parent, view)) {
        outRect.right = spacingRightLast
      }
    }
  }

  private fun isFirstItem(parent: RecyclerView, view: View): Boolean =
    parent.getChildAdapterPosition(view) == 0

  private fun isLastItem(parent: RecyclerView, view: View): Boolean =
    parent.getChildAdapterPosition(view) == parent.childCount

}