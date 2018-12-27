package com.illiarb.tmdbclient.feature.search.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.illiarb.tmdbclient.feature.search.R
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.adapter.BaseAdapter
import com.illiarb.tmdbexplorer.coreui.ext.inflate
import com.illiarb.tmdbexplorer.coreui.pipeline.MoviePipelineData
import com.illiarb.tmdbexplorer.coreui.pipeline.UiPipelineData
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.pipeline.EventPipeline
import javax.inject.Inject

/**
 * @author ilya-rb on 27.12.18.
 */
class SearchAdapter @Inject constructor(
    private val uiEventsPipeline: EventPipeline<@JvmSuppressWildcards UiPipelineData>
) : BaseAdapter<Movie, SearchResultViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder =
        SearchResultViewHolder(parent.inflate(R.layout.item_search_result))
            .apply {
                itemView.setOnClickListener {
                    uiEventsPipeline.dispatchEvent(MoviePipelineData(getItem(adapterPosition)))
                }
            }
}