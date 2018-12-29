package com.illiarb.tmdbclient.dynamic.feature.account.profile.ui.adapter

import android.view.ViewGroup
import com.illiarb.tmdbclient.dynamic.feature.account.R
import com.illiarb.tmdbclient.dynamic.feature.account.profile.ui.MovieViewHolder
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.adapter.BaseAdapter
import com.illiarb.tmdbexplorer.coreui.ext.inflate
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import com.illiarb.tmdbexplorer.coreui.pipeline.MoviePipelineData
import com.illiarb.tmdbexplorer.coreui.pipeline.UiPipelineData
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.pipeline.EventPipeline
import javax.inject.Inject

/**
 * @author ilya-rb on 06.12.18.
 */
class FavoritesAdapter @Inject constructor(
    private val uiEventPipeline: EventPipeline<@JvmSuppressWildcards UiPipelineData>,
    private val imageLoader: ImageLoader
) : BaseAdapter<Movie, MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(parent.inflate(R.layout.item_movie), imageLoader)

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnClickListener {
            uiEventPipeline.dispatchEvent(MoviePipelineData(getItemAt(position)))
        }
    }
}