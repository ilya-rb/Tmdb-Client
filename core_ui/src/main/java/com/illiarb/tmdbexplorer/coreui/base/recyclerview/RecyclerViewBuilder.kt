package com.illiarb.tmdbexplorer.coreui.base.recyclerview

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.decoration.SpaceItemDecoration

/**
 * @author ilya-rb on 28.12.18.
 */
@Suppress("unused")
class RecyclerViewBuilder {

    private var orientation: LayoutOrientation = LayoutOrientation.VERTICAL
    private var type: LayoutType = LayoutType.Linear
    private var adapter: RecyclerView.Adapter<*>? = null
    private var hasFixedSize: Boolean = false
    private var spaceBetween: SpaceBetween? = null

    fun orientation(orientation: LayoutOrientation): RecyclerViewBuilder {
        this.orientation = orientation
        return this
    }

    fun type(type: LayoutType): RecyclerViewBuilder {
        this.type = type
        return this
    }

    fun adapter(adapter: RecyclerView.Adapter<*>): RecyclerViewBuilder {
        this.adapter = adapter
        return this
    }

    fun hasFixedSize(hasFixedSize: Boolean): RecyclerViewBuilder {
        this.hasFixedSize = hasFixedSize
        return this
    }

    fun spaceBetween(block: SpaceBetween.() -> Unit): RecyclerViewBuilder {
        spaceBetween = SpaceBetween().also(block)
        return this
    }

    fun attachToRecyclerView(recyclerView: RecyclerView) {
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

            spaceBetween?.let {
                addItemDecoration(SpaceItemDecoration(it.horizontally, it.vertically))
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
    var vertically: Int = 0,
    var horizontally: Int = 0,
    var addToFirst: Boolean = true,
    var addToLast: Boolean = true
)

enum class LayoutOrientation {
    VERTICAL,
    HORIZONTAL
}

sealed class LayoutType {
    object Linear : LayoutType()
    data class Grid(val spanCount: Int) : LayoutType()
}