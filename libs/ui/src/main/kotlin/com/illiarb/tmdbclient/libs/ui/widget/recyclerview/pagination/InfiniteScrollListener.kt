package com.illiarb.tmdbclient.libs.ui.widget.recyclerview.pagination

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A scroll listener for RecyclerView to load more items as you approach the end.
 */
abstract class InfiniteScrollListener(
  private val layoutManager: GridLayoutManager
) : RecyclerView.OnScrollListener() {

  override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
    // bail out if scrolling upward
    if (dy < 0) {
      return
    }

    val lastItem = layoutManager.findLastCompletelyVisibleItemPosition()
    val currentTotalCount = layoutManager.itemCount

    if (currentTotalCount <= lastItem + VISIBLE_THRESHOLD) {
      onLoadMore()
    }
  }

  abstract fun onLoadMore()

  companion object {

    // The minimum number of items remaining before we should loading more.
    private const val VISIBLE_THRESHOLD = 3
  }
}