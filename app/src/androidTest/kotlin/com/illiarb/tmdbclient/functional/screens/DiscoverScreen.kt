package com.illiarb.tmdbclient.functional.screens

import com.agoda.kakao.common.views.KView
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.swiperefresh.KSwipeRefreshLayout
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.functional.recyclerview.MovieItem
import com.illiarb.tmdbclient.modules.discover.DiscoverFragment

object DiscoverScreen : KScreen<DiscoverScreen>() {

  override val layoutId: Int?
    get() = R.layout.fragment_discover

  override val viewClass: Class<*>
    get() = DiscoverFragment::class.java

  val root = KSwipeRefreshLayout { withId(R.id.discoverSwipeRefresh) }

  val toolbar = KView { withId(R.id.toolbar) }

  val recyclerView = KRecyclerView(
    { withId(R.id.discoverList) },
    { itemType(::MovieItem) }
  )
}