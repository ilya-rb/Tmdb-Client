package com.illiarb.tmdbclient.feature.search.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.illiarb.tmdbclient.feature.search.R
import com.illiarb.tmdbexplorer.coreui.ext.inflate
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import com.illiarb.tmdbexplorer.coreui.recyclerview.adapter.BaseDiffAdapter
import com.illiarb.tmdblcient.core.domain.entity.Movie
import javax.inject.Inject

/**
 * @author ilya-rb on 27.12.18.
 */
class SearchAdapter @Inject constructor(
    private val imageLoader: ImageLoader
) : BaseDiffAdapter<Movie, SearchResultViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder =
        SearchResultViewHolder(parent.inflate(R.layout.item_search_result), imageLoader)
}