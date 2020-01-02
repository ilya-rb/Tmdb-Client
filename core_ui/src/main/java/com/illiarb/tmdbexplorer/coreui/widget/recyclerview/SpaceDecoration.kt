package com.illiarb.tmdbexplorer.coreui.widget.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author ilya-rb on 18.01.19.
 */
class SpaceDecoration(
    private val spacingLeft: Int = 0,
    private val spacingRight: Int = 0,
    private val spacingTop: Int = 0,
    private val spacingBottom: Int = 0
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = spacingLeft
        outRect.right = spacingRight
        outRect.bottom = spacingBottom
        outRect.top = spacingTop
    }
}