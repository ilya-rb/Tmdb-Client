package com.illiarb.tmdbclient.dynamic.feature.account.profile.ui.adapter

import android.view.ViewGroup
import com.illiarb.tmdbclient.dynamic.feature.account.R
import com.illiarb.tmdbclient.dynamic.feature.account.profile.ui.MovieViewHolder
import com.illiarb.tmdbexplorer.coreui.ext.inflate
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import com.illiarb.tmdbexplorer.coreui.recyclerview.adapter.BaseAdapter
import com.illiarb.tmdblcient.core.domain.entity.Movie
import javax.inject.Inject

/**
 * @author ilya-rb on 06.12.18.
 */
class FavoritesAdapter @Inject constructor(
    private val imageLoader: ImageLoader
) : BaseAdapter<Movie, MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(parent.inflate(R.layout.item_movie), imageLoader)
}