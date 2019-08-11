package com.illiarb.tmdbclient.movies.home.adapter

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.illiarb.core_ui_image.ImageLoader
import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.domain.entity.MovieSection
import javax.inject.Inject

class MovieAdapter @Inject constructor(imageLoader: ImageLoader) : ListDelegationAdapter<List<MovieSection>>() {

    var onClickBlock: (Movie) -> Unit = {}

    private val onClick: (Movie) -> Unit = {
        onClickBlock(it)
    }

    init {
        delegatesManager.addDelegate(movieSectionDelegate(imageLoader, onClick))
    }
}