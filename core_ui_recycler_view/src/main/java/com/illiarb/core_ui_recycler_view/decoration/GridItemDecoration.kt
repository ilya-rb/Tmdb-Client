package com.illiarb.core_ui_recycler_view.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridItemDecoration(
    private val spacing: Int,
    private val columnCount: Int
) : RecyclerView.ItemDecoration() {

    private var isNeedLeftSpacing = false

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val frameWidth =
            ((parent.width - spacing.toFloat() * (columnCount - 1)) / columnCount).toInt()

        val padding: Int = parent.width / columnCount - frameWidth
        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition

        if (itemPosition < columnCount) {
            outRect.top = 0
        } else {
            outRect.top = spacing
        }

        if (itemPosition % columnCount == 0) {
            outRect.left = 0
            outRect.right = padding
            isNeedLeftSpacing = true
        } else if ((itemPosition + 1) % columnCount == 0) {
            isNeedLeftSpacing = false
            outRect.right = 0
            outRect.left = padding
        } else if (isNeedLeftSpacing) {
            isNeedLeftSpacing = false
            outRect.left = spacing - padding
            if ((itemPosition + 2) % columnCount == 0) {
                outRect.right = spacing - padding
            } else {
                outRect.right = spacing / 2
            }
        } else if ((itemPosition + 2) % columnCount == 0) {
            isNeedLeftSpacing = false
            outRect.left = spacing / 2
            outRect.right = spacing - padding
        } else {
            isNeedLeftSpacing = false
            outRect.left = spacing / 2
            outRect.right = spacing / 2
        }
        outRect.bottom = 0
    }
}