package com.illiarb.tmdbclient.feature.home.list.ui.delegates.nowplaying

import android.view.View
import android.view.ViewGroup
import com.illiarb.tmdbclient.feature.home.R
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.adapter.BaseAdapter
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.viewholder.BaseViewHolder
import com.illiarb.tmdbexplorer.coreui.ext.inflate
import com.illiarb.tmdbexplorer.coreui.image.CropOptions
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import com.illiarb.tmdbexplorer.coreui.image.RequestOptions
import com.illiarb.tmdbexplorer.coreui.pipeline.MoviePipelineData
import com.illiarb.tmdbexplorer.coreui.pipeline.UiPipelineData
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.pipeline.EventPipeline
import kotlinx.android.synthetic.main.item_now_playing.view.*

class NowPlayingPagerAdapter(
    private val uiEventsPipeline: EventPipeline<UiPipelineData>,
    private val imageLoader: ImageLoader
) : BaseAdapter<Movie, NowPlayingPagerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_now_playing), imageLoader)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnClickListener {
            uiEventsPipeline.dispatchEvent(MoviePipelineData(getItemAt(position)))
        }
    }

    class ViewHolder(
        containerView: View,
        private val imageLoader: ImageLoader
    ) : BaseViewHolder<Movie>(containerView) {

        private val radius = itemView.resources.getDimensionPixelSize(R.dimen.image_corner_radius)
        private val itemCover = itemView.itemNowPlayingCover

        override fun bind(item: Movie) {
            imageLoader.fromUrl(item.posterPath, itemCover, RequestOptions.create {
                cornerRadius(radius)
                cropOptions(CropOptions.CENTER_CROP)
            })
        }

        override fun onViewRecycled() {
            imageLoader.clearTarget(itemCover)
        }
    }
}