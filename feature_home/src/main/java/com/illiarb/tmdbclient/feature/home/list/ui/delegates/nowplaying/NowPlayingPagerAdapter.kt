package com.illiarb.tmdbclient.feature.home.list.ui.delegates.nowplaying

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.illiarb.tmdbclient.feature.home.R
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.adapter.BaseAdapter
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.viewholder.BaseViewHolder
import com.illiarb.tmdbexplorer.coreui.ext.inflate
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import com.illiarb.tmdbexplorer.coreui.pipeline.MoviePipelineData
import com.illiarb.tmdbexplorer.coreui.pipeline.UiPipelineData
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.pipeline.EventPipeline
import kotlinx.android.synthetic.main.item_now_playing.view.*

class NowPlayingPagerAdapter(
    private val uiEventsPipeline: EventPipeline<UiPipelineData>
) : BaseAdapter<Movie, NowPlayingPagerAdapter.NowPlayingViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NowPlayingViewHolder {
        return NowPlayingViewHolder(parent.inflate(R.layout.item_now_playing))
    }

    override fun onBindViewHolder(holder: NowPlayingViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnClickListener {
            uiEventsPipeline.dispatchEvent(MoviePipelineData(getItem(position)))
        }
    }

    class NowPlayingViewHolder(containerView: View) : BaseViewHolder<Movie>(containerView) {

        private val radius = itemView.resources.getDimensionPixelSize(R.dimen.image_corner_radius)
        private val itemCover = itemView.itemNowPlayingCover

        override fun bind(item: Movie) {
            ImageLoader.loadImage(itemCover, item.posterPath, true, radius)
        }

        override fun onViewRecycled() {
            ImageLoader.clearImageView(itemCover)
        }
    }
}