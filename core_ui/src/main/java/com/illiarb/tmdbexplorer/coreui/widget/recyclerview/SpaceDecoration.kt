package com.illiarb.tmdbexplorer.coreui.widget.recyclerview

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

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        fun isFirst(): Boolean = parent.getChildAdapterPosition(view) == 0
        fun isLast(): Boolean = parent.getChildAdapterPosition(view) == parent.childCount

        outRect.setEmpty()

        outRect.left = spacingLeft
        outRect.right = spacingRight
        outRect.top = spacingTop
        outRect.bottom = spacingBottom

        if (orientation == LinearLayoutManager.VERTICAL) {
            if (isFirst()) {
                outRect.top = spacingTopFirst
            }

            if (isLast()) {
                outRect.bottom = spacingBottomLast
            }
        } else {
            if (isFirst()) {
                outRect.left = spacingLeftFirst
            }

            if (isLast()) {
                outRect.right = spacingRightLast
            }
        }
    }
}