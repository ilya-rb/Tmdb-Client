package com.illiarb.tmdbexplorer.functional.screens

import android.view.View
import com.agoda.kakao.chipgroup.KChipGroup
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.text.KTextView
import com.illiarb.tmdbclient.home.HomeFragment
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.functional.recyclerview.MovieItem
import org.hamcrest.Matcher

object HomeScreen : KScreen<HomeScreen>() {

  override val layoutId: Int?
    get() = R.layout.fragment_movies

  override val viewClass: Class<*>
    get() = HomeFragment::class.java

  val moviesList = KRecyclerView(
    { withId(R.id.moviesList) },
    {
      itemType(::NowPlayingSectionItem)
      itemType(::TrendingSectionItem)
      itemType(::GenreSectionItem)
      itemType(::MovieSectionItem)
    }
  )

  fun openDiscoverScreen() {
    moviesList.firstChild<GenreSectionItem> {
      genres.selectChipAt(0)
    }
  }

  fun checkIsVisible() {
    moviesList {
      isVisible()
    }
  }

  class MovieSectionItem(parent: Matcher<View>) : KRecyclerItem<MovieSectionItem>(parent) {
    val title = KTextView { withId(R.id.itemSectionTitle) }
    val movies = KRecyclerView(
      { withId(R.id.itemMovieSectionList) },
      { itemType(::MovieItem) }
    )
  }

  class TrendingSectionItem(parent: Matcher<View>) : KRecyclerItem<TrendingSectionItem>(parent) {
    val items = KRecyclerView({ withId(R.id.itemTrendingSectionList) }, {})
  }

  class NowPlayingSectionItem(parent: Matcher<View>) : KRecyclerItem<NowPlayingSectionItem>(parent) {
    val items = KRecyclerView(
      { withId(R.id.nowPlayingPager) },
      { itemType(::NowPlayingItem) }
    )

    class NowPlayingItem(parent: Matcher<View>) : KRecyclerItem<NowPlayingItem>(parent)
  }

  class GenreSectionItem(parent: Matcher<View>) : KRecyclerItem<GenreSectionItem>(parent) {
    val genres = KChipGroup { withId(R.id.genresChipGroup) }
  }
}