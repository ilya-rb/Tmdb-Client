package com.illiarb.tmdbexplorer.coreui.recyclerview

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.illiarb.tmdbexplorer.coreui.recyclerview.decoration.HorizontalDecoration
import com.illiarb.tmdbexplorer.coreui.recyclerview.decoration.VerticalDecoration

/**
 * @author ilya-rb on 28.12.18.
 */
@Suppress("unused")
class RecyclerViewBuilder {

    private var orientation: LayoutOrientation = LayoutOrientation.VERTICAL
    private var type: LayoutType = LayoutType.Linear()
    private var adapter: RecyclerView.Adapter<*>? = null
    private var hasFixedSize: Boolean = false
    private var spaceBetween: SpaceBetween? = null
    private var nestedScrollEnabled = true

    fun orientation(orientation: LayoutOrientation) = apply { this.orientation = orientation }

    fun type(type: LayoutType) = apply { this.type = type }

    fun adapter(adapter: RecyclerView.Adapter<*>) = apply { this.adapter = adapter }

    fun hasFixedSize(hasFixedSize: Boolean) = apply { this.hasFixedSize = hasFixedSize }

    fun spaceBetween(block: SpaceBetween.() -> Unit) = apply {
        this.spaceBetween = SpaceBetween().also(block)
    }

    fun disableNestedScroll() = apply { this.nestedScrollEnabled = false }

    fun setupWith(recyclerView: RecyclerView) {
        recyclerView.apply {
            val context = recyclerView.context

            adapter = this@RecyclerViewBuilder.adapter
            layoutManager = when (type) {
                is LayoutType.Linear -> LinearLayoutManager(context, createLinearOrientation(), false)
                is LayoutType.Grid -> {
                    val grid = type as LayoutType.Grid
                    GridLayoutManager(context, grid.spanCount, createGridOrientation(), false)
                }
            }

            isNestedScrollingEnabled = nestedScrollEnabled

            spaceBetween?.let {
                if (type is LayoutType.Linear) {
                    when (orientation) {
                        LayoutOrientation.HORIZONTAL -> addItemDecoration(HorizontalDecoration(it.spacing))
                        LayoutOrientation.VERTICAL -> addItemDecoration(VerticalDecoration(it.spacing))
                    }
                }
            }
        }
    }

    private fun createGridOrientation(): Int =
        when (orientation) {
            LayoutOrientation.VERTICAL -> GridLayoutManager.VERTICAL
            LayoutOrientation.HORIZONTAL -> GridLayoutManager.HORIZONTAL
        }

    private fun createLinearOrientation(): Int =
        when (orientation) {
            LayoutOrientation.VERTICAL -> LinearLayoutManager.VERTICAL
            LayoutOrientation.HORIZONTAL -> LinearLayoutManager.HORIZONTAL
        }

    companion object {

        inline fun create(builder: RecyclerViewBuilder.() -> RecyclerViewBuilder): RecyclerViewBuilder {
            return builder(RecyclerViewBuilder())
        }
    }
}

data class SpaceBetween(
    var spacing: Int = 0,
    var addToFirst: Boolean = true,
    var addToLast: Boolean = true
)

enum class LayoutOrientation {
    VERTICAL,
    HORIZONTAL
}

sealed class LayoutType {

    companion object {
        const val DEFAULT_PREFETCH_COUNT = 4
    }

    data class Linear(val prefetchCount: Int = -1) : LayoutType()
    data class Grid(val spanCount: Int) : LayoutType()
}