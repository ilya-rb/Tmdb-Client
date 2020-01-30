package com.illiarb.tmdbclient.home.delegates

import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.coreuiimage.loadImage
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdbexplorer.coreui.ext.setVisible
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.LoopingPagerAdapter
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.domain.NowPlayingSection
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

// 5 seconds
private const val TIMER_IMAGE_UPDATE = 5000L

fun nowPlayingDelegate(clickListener: OnClickListener) =
    adapterDelegate<NowPlayingSection, MovieSection>(R.layout.item_now_playing_section) {

        val nowPlayingPager = itemView.findViewById<RecyclerView>(R.id.nowPlayingPager)
        val nowPlayingTitle = itemView.findViewById<TextView>(R.id.nowPlayingTitle)
        val nowPlayingCurrentItem = itemView.findViewById<TextView>(R.id.nowPlayingCurrentItem)

        val adapter = NowPlayingPagerAdapter(clickListener)
        val snapHelper = PagerSnapHelper()
        val mainHandler = Handler(Looper.getMainLooper())

        var imagesTimer: Timer? = null
        var currentPosition = 0

        snapHelper.attachToRecyclerView(nowPlayingPager)

        nowPlayingPager.adapter = adapter
        nowPlayingPager.layoutManager =
            LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

        nowPlayingPager.addItemDecoration(
            SpaceDecoration.edgeInnerSpace(0, itemView.dimen(R.dimen.spacing_micro))
        )

        fun updateCurrentPosition(position: Int) {
            mainHandler.post {
                nowPlayingCurrentItem.text = String.format("%d/%d", position + 1, item.movies.size)
            }
        }

        bind {
            nowPlayingTitle.text = item.title
            updateCurrentPosition(currentPosition)

            adapter.items = item.movies
            adapter.notifyDataSetChanged()
        }

        onViewAttachedToWindow {
            imagesTimer = fixedRateTimer(initialDelay = TIMER_IMAGE_UPDATE, period = TIMER_IMAGE_UPDATE) {
                if (adapter.realCount == 0) {
                    return@fixedRateTimer
                }
                currentPosition++
                updateCurrentPosition(adapter.getRealPosition(currentPosition))
                nowPlayingPager.smoothScrollToPosition(currentPosition)
            }
        }

        onViewDetachedFromWindow {
            imagesTimer?.cancel()
            mainHandler.removeCallbacksAndMessages(null)
        }
    }

private class NowPlayingPagerAdapter(private val clickListener: OnClickListener) :
    ListDelegationAdapter<List<Movie>>(), LoopingPagerAdapter {

    override val realCount: Int
        get() = items.size

    init {
        delegatesManager.addDelegate(nowPlayingDelegate())
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

    private fun nowPlayingDelegate() = adapterDelegate<Movie, Movie>(R.layout.item_now_playing_item) {

        val title = itemView.findViewById<TextView>(R.id.itemNowPlayingTitle)
        val cover = itemView.findViewById<ImageView>(R.id.itemNowPlayingCover)
        val poster = itemView.findViewById<ImageView>(R.id.itemNowPlayingPoster)
        val rating = itemView.findViewById<TextView>(R.id.itemNowPlayingRating)
        val spacing = itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_normal)

        bind {
            title.text = item.title

            rating.setVisible(item.voteAverage > 0)
            rating.text = item.voteAverage.toString()

            cover.loadImage(item.backdropPath) {
                cornerRadius(spacing)
                blur()
            }

            poster.loadImage(item.posterPath) {
                cornerRadius(spacing)
            }

            itemView.setOnClickListener {
                clickListener.onClick(item)
            }
        }
    }
}