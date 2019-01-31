package com.illiarb.tmdbclient.feature.home.list.ui.delegates.nowplaying

import android.view.View
import android.view.ViewGroup
import com.illiarb.tmdbclient.feature.home.R
import com.illiarb.tmdbexplorer.coreui.common.ViewClickListener
import com.illiarb.tmdbexplorer.coreui.ext.inflate
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import com.illiarb.tmdbexplorer.coreui.recyclerview.adapter.AdapterDelegate
import com.illiarb.tmdbexplorer.coreui.recyclerview.viewholder.BaseDelegateViewHolder
import com.illiarb.tmdblcient.core.domain.entity.NowPlayingSection
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.item_now_playing_section.view.*
import javax.inject.Inject

class NowPlayingSectionDelegate @Inject constructor(
    private val imageLoader: ImageLoader,
    private val viewClickListener: ViewClickListener
) : AdapterDelegate {

    override fun isForViewType(item: Any): Boolean = item is NowPlayingSection

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseDelegateViewHolder =
        ViewHolder(parent.inflate(R.layout.item_now_playing_section), imageLoader, viewClickListener)

    class ViewHolder(
        containerView: View,
        imageLoader: ImageLoader,
        viewClickListener: ViewClickListener
    ) : BaseDelegateViewHolder(containerView) {

        private val adapter = NowPlayingPagerAdapter(imageLoader, viewClickListener)
        private val sectionPager = itemView.itemNowPlayingSectionPager

        init {
            sectionPager.let {
                it.adapter = adapter
                it.setOverScrollEnabled(true)
                it.overScrollMode = DiscreteScrollView.OVER_SCROLL_ALWAYS
                it.setHasFixedSize(true)
                it.setSlideOnFling(true)
                it.setItemTransformer(
                    ScaleTransformer.Builder()
                        .setMinScale(0.8f)
                        .setPivotX(Pivot.X.CENTER)
                        .setPivotY(Pivot.Y.BOTTOM)
                        .build()
                )
            }
        }

        override fun bind(item: Any) {
            if (item !is NowPlayingSection) {
                return
            }
            adapter.submitList(item.movies)
        }

        override fun onViewRecycled() {
        }
    }
}