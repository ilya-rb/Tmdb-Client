package com.illiarb.core_ui_recycler_view.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author ilya-rb on 18.01.19.
 */
class HorizontalDecoration(
    private val spacingLeft: Int = 0,
    private val spacingRight: Int = 0
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = spacingLeft
        outRect.right = spacingRight
    }
}