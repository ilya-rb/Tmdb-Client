package com.illiarb.tmdbclient.home.ui

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.illiarb.core_ui_image.ImageLoader
import com.illiarb.tmdblcient.core.domain.entity.MovieSection
import javax.inject.Inject

class MovieAdapter @Inject constructor(imageLoader: ImageLoader) : ListDelegationAdapter<List<MovieSection>>() {

    init {
        delegatesManager.addDelegate(movieSectionDelegate(imageLoader))
    }
}