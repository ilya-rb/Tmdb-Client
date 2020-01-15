package com.illiarb.tmdbexplorer.functional.screens

import android.view.View
import com.agoda.kakao.common.views.KView
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.text.KButton
import com.agoda.kakao.text.KTextView
import com.illiarb.tmdbclient.home.HomeFragment
import com.illiarb.tmdbclient.movies.home.R
import org.hamcrest.Matcher

object HomeScreen : KScreen<HomeScreen>() {

    override val layoutId: Int?
        get() = R.layout.fragment_movies

    override val viewClass: Class<*>
        get() = HomeFragment::class.java

    val settingsIcon = KView { withId(R.id.moviesSettings) }

    val moviesList = KRecyclerView({ withId(R.id.moviesList) }, {
        itemType(::MovieSectionItem)
    })

    class MovieSectionItem(parent: Matcher<View>) : KRecyclerItem<MovieSectionItem>(parent) {

        val title = KTextView { withId(R.id.itemSectionTitle) }
        val seeAll = KButton { withId(R.id.itemSectionSeeAll) }
        val movies = KRecyclerView({ withId(R.id.itemMovieSectionList) }, {})
    }
}