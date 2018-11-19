package com.illiarb.tmdbexplorer.coreui.base.recyclerview.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author ilya-rb on 26.10.18.
 */
class GridItemDecoration(
    private val spacing: Int,
    private val spanCount: Int
) : RecyclerView.ItemDecoration() {

    private var mNeedLeftSpacing = false

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val frameWidth = ((parent.width - spacing.toFloat() * (spanCount - 1)) / spanCount).toInt()
        val padding = parent.width / spanCount - frameWidth

        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
        if (itemPosition < spanCount) {
            outRect.top = 0
        } else {
            outRect.top = spacing
        }

        if (itemPosition % spanCount == 0) {
            outRect.left = 0
            outRect.right = padding
            mNeedLeftSpacing = true
        } else if ((itemPosition + 1) % spanCount == 0) {
            mNeedLeftSpacing = false
            outRect.right = 0
            outRect.left = padding
        } else if (mNeedLeftSpacing) {
            mNeedLeftSpacing = false
            outRect.left = spacing - padding
            if ((itemPosition + 2) % spanCount == 0) {
                outRect.right = spacing - padding
            } else {
                outRect.right = spacing / 2
            }
        } else if ((itemPosition + 2) % spanCount == 0) {
            mNeedLeftSpacing = false
            outRect.left = spacing / 2
            outRect.right = spacing - padding
        } else {
            mNeedLeftSpacing = false
            outRect.left = spacing / 2
            outRect.right = spacing / 2
        }
        outRect.bottom = 0
    }
}