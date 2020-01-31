package com.illiarb.tmdbclient.home.delegates

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.LoopingPagerAdapter
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.domain.NowPlayingSection
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

// 10 seconds
private const val TIMER_IMAGE_UPDATE = 10000L

fun nowPlayingSectionDelegate(clickListener: OnClickListener) =
    adapterDelegate<NowPlayingSection, MovieSection>(R.layout.item_now_playing_section) {

        val nowPlayingPager = itemView.findViewById<RecyclerView>(R.id.nowPlayingPager)
        val adapter = NowPlayingPagerAdapter(clickListener)
        val snapHelper = PagerSnapHelper()

        var imagesTimer: Timer? = null
        var currentPosition = 0

        snapHelper.attachToRecyclerView(nowPlayingPager)

        nowPlayingPager.adapter = adapter
        nowPlayingPager.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

        bind {
            adapter.items = item.movies
            adapter.notifyDataSetChanged()
        }

        onViewAttachedToWindow {
            imagesTimer = fixedRateTimer(initialDelay = TIMER_IMAGE_UPDATE, period = TIMER_IMAGE_UPDATE) {
                if (adapter.realCount == 0) {
                    return@fixedRateTimer
                }
                currentPosition++
                nowPlayingPager.smoothScrollToPosition(currentPosition)
            }
        }

        onViewDetachedFromWindow {
            imagesTimer?.cancel()
        }
    }

private class NowPlayingPagerAdapter(clickListener: OnClickListener) :
    ListDelegationAdapter<List<Movie>>(), LoopingPagerAdapter {

    override val realCount: Int
        get() = items.size

    init {
        delegatesManager.addDelegate(nowPlayingItemDelegate(clickListener))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, getRealPosition(position))
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(getRealPosition(position))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any?>) {
        super.onBindViewHolder(holder, getRealPosition(position), payloads)
    }

    override fun getItemCount(): Int = if (realCount <= 1) realCount else LoopingPagerAdapter.MAX_COUNT
}