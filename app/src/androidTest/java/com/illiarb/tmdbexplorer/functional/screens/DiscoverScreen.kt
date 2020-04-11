package com.illiarb.tmdbexplorer.functional.screens

import androidx.test.espresso.action.ViewActions
import com.agoda.kakao.chipgroup.KChipGroup
import com.agoda.kakao.common.views.KView
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.swiperefresh.KSwipeRefreshLayout
import com.agoda.kakao.text.KButton
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.ui.discover.DiscoverFragment
import com.illiarb.tmdbexplorer.functional.recyclerview.MovieItem

object DiscoverScreen : KScreen<DiscoverScreen>() {

  override val layoutId: Int?
    get() = R.layout.fragment_discover

  override val viewClass: Class<*>
    get() = DiscoverFragment::class.java

  val root = KSwipeRefreshLayout { withId(R.id.discoverSwipeRefresh) }

  val filterGenres = KChipGroup { withId(R.id.discoverGenres) }

  val applyFilter = KButton { withId(R.id.discoverApplyFilter) }

  val toolbar = KView { withId(R.id.toolbar) }

  val recyclerView = KRecyclerView(
    { withId(R.id.discoverList) },
    { itemType(::MovieItem) }
  )

  private val filtersContainer = KView { withId(R.id.discoverFiltersContainer) }

  fun swipeFiltersToTop() {
    filtersContainer.view.perform(ViewActions.swipeUp())
  }
}