package com.illiarb.tmdbclient.functional.screens

import com.agoda.kakao.common.views.KView
import com.agoda.kakao.image.KImageView
import com.agoda.kakao.recycler.KRecyclerView
import com.illiarb.tmdbclient.home.HomeFragment
import com.illiarb.tmdbclient.movies.home.R

object HomeScreen : KScreen<HomeScreen>() {

    override val layoutId: Int?
        get() = R.layout.fragment_movies

    override val viewClass: Class<*>
        get() = HomeFragment::class.java

    val appBar = KView { withId(R.id.appBar) }
    val moviesList = KRecyclerView({ withId(R.id.moviesList) }, {})
    val searchIcon = KImageView { withId((R.id.moviesSearch)) }
    val accountIcon = KImageView { withId(R.id.moviesAccount) }
}