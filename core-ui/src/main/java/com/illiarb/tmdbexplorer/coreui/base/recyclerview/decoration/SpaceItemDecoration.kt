package com.illiarb.tmdbexplorer.coreui.base.recyclerview.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(
    private val horizontalSpacing: Int,
    private val verticalSpacing: Int,
    private val addFirst: Boolean = true,
    private val addLast: Boolean = true
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = horizontalSpacing
        outRect.right = horizontalSpacing

        if (parent.getChildAdapterPosition(view) == 0) {
            if (addFirst) {
                outRect.top = verticalSpacing
            }
        }

        if (parent.getChildAdapterPosition(view) == parent.childCount - 1) {
            if (addLast) {
                outRect.bottom = verticalSpacing
            }
        } else {
            outRect.bottom = verticalSpacing
        }
    }
}