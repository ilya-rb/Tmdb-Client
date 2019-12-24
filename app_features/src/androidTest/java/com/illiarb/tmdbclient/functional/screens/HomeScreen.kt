package com.illiarb.tmdbclient.functional.screens

import com.agoda.kakao.common.views.KView
import com.agoda.kakao.image.KImageView
import com.agoda.kakao.recycler.KRecyclerView
import com.illiarb.tmdbclient.movies.home.R

object HomeScreen {

    val appBar = KView { withId(R.id.appBar) }

    val moviesList = KRecyclerView({ withId(R.id.moviesList) }, {})

    val searchIcon = KImageView { withId((R.id.moviesSearch)) }
}