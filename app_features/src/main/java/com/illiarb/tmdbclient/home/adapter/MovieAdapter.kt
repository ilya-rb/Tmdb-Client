package com.illiarb.tmdbclient.home.adapter

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.illiarb.core_ui_image.ImageLoader
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieSection
import javax.inject.Inject

class MovieAdapter @Inject constructor(
    imageLoader: ImageLoader,
    clickListener: OnClickListener
) : ListDelegationAdapter<List<MovieSection>>() {

    init {
        delegatesManager.addDelegate(movieSectionDelegate(imageLoader, clickListener))
        delegatesManager.addDelegate(nowPlayingDelegate(imageLoader))
        delegatesManager.addDelegate(genresSectionDelegate())
    }
}