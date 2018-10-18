package com.illiarb.tmdbclient.feature.movies.movieslist.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.adapter.BaseAdapter
import com.illiarb.tmdbexplorer.coreui.ext.inflate
import com.illiarb.tmdblcient.core.entity.Movie
import javax.inject.Inject

class MoviesAdapter @Inject constructor() : BaseAdapter<Movie, MovieViewHolder>(diffCallback) {

    companion object {

        @JvmStatic
        private val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(parent.inflate(R.layout.item_movie))
}