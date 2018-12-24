package com.illiarb.tmdbclient.feature.movies.list.ui.delegates.nowplaying

import android.view.View
import android.view.ViewGroup
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.adapter.AdapterDelegate
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.viewholder.BaseDelegateViewHolder
import com.illiarb.tmdbexplorer.coreui.ext.inflate
import com.illiarb.tmdbexplorer.coreui.pipeline.UiPipelineData
import com.illiarb.tmdblcient.core.entity.NowPlayingSection
import com.illiarb.tmdblcient.core.pipeline.EventPipeline
import kotlinx.android.synthetic.main.item_now_playing_section.view.*
import kotlinx.android.synthetic.main.layout_section_title.view.*
import javax.inject.Inject

class NowPlayingSectionDelegate @Inject constructor(
    private val uiEventsPipeline: EventPipeline<@JvmSuppressWildcards UiPipelineData>
) : AdapterDelegate {

    override fun isForViewType(item: Any): Boolean = item is NowPlayingSection

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseDelegateViewHolder =
        ViewHolder(
            parent.inflate(R.layout.item_now_playing_section),
            uiEventsPipeline
        )

    class ViewHolder(
        containerView: View,
        uiEventsPipeline: EventPipeline<UiPipelineData>
    ) : BaseDelegateViewHolder(containerView) {

        private val sectionTitle = itemView.itemSectionTitle
        private val sectionMore = itemView.itemSectionMore
        private val sectionPager = itemView.itemNowPlayingSectionPager

        private val adapter = NowPlayingPagerAdapter(uiEventsPipeline)

        init {
            sectionPager.adapter = adapter
            sectionPager.pageMargin =
                itemView.resources.getDimensionPixelSize(R.dimen.item_movie_spacing) / 2
        }

        override fun bind(item: Any) {
            if (item !is NowPlayingSection) {
                return
            }
            sectionTitle.text = item.title
            adapter.setMovies(item.movies)
        }

        override fun bindClickListener(clickListener: View.OnClickListener) {
            sectionMore.setOnClickListener(clickListener)
        }

        override fun onViewRecycled() {
        }
    }
}