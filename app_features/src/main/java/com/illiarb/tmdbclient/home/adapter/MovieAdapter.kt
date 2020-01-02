package com.illiarb.tmdbclient.home.adapter

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdblcient.core.domain.MovieSection

class MovieAdapter(
    private val clickListener: OnClickListener = OnClickListener.DefaultOnClickListener()
) : ListDelegationAdapter<List<MovieSection>>(), OnClickListener by clickListener {

    init {
        delegatesManager.addDelegate(movieSectionDelegate(clickListener))
        delegatesManager.addDelegate(nowPlayingDelegate(clickListener))
        delegatesManager.addDelegate(genresSectionDelegate(clickListener))
    }
}