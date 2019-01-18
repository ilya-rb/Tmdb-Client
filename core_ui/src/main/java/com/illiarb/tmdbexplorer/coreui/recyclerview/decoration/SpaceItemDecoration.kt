package com.illiarb.tmdbexplorer.coreui.recyclerview.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(
    private val spacing: Int,
    private val addFirst: Boolean = true,
    private val addLast: Boolean = true,
    private val mainAxis: MainAxis = MainAxis.Y
) : RecyclerView.ItemDecoration() {

    private fun getElementPosition(parent: RecyclerView, view: View): ElementPosition {
        return when (parent.getChildAdapterPosition(view)) {
            0 -> ElementPosition.FIRST
            parent.childCount - 1 -> ElementPosition.LAST
            else -> ElementPosition.MIDDLE
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = getElementPosition(parent, view)

        when (position) {
            ElementPosition.FIRST -> {
                if (addFirst) {
                    when (mainAxis) {
                        MainAxis.X -> outRect.left = spacing
                        MainAxis.Y -> outRect.top = spacing
                    }
                } else {
                    outRect.left = 0
                    outRect.top = 0
                }
            }

            ElementPosition.MIDDLE -> {
                when (mainAxis) {
                    MainAxis.X -> outRect.left = spacing
                    MainAxis.Y -> outRect.top = spacing
                }
            }

            ElementPosition.LAST -> {
                if (addLast) {
                    when (mainAxis) {
                        MainAxis.X -> outRect.right = spacing
                        MainAxis.Y -> outRect.bottom = spacing
                    }
                } else {
                    outRect.right = 0
                    outRect.bottom = 0
                }
            }
        }
    }

    enum class ElementPosition {
        FIRST,
        MIDDLE,
        LAST
    }

    enum class MainAxis {
        X,
        Y
    }
}