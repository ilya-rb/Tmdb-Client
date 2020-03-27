package com.illiarb.tmdbclient.home.delegates.nowplaying

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.RecyclerViewStateSaver
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.StateSaver
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.domain.NowPlayingSection
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

// 10 seconds
private const val TIMER_IMAGE_UPDATE = 10000L
private const val KEY_NOW_PLAYING_POSITION = "now_playing_position"

fun nowPlayingSection(
  stateSaver: RecyclerViewStateSaver,
  clickListener: OnClickListener<Movie>
) = adapterDelegate<NowPlayingSection, MovieSection>(R.layout.item_now_playing_section) {

  val nowPlayingPager = itemView.findViewById<RecyclerView>(R.id.nowPlayingPager)
  val adapter =
    NowPlayingPagerAdapter(clickListener)
  val snapHelper = PagerSnapHelper()

  var imagesTimer: Timer? = null
  var currentPosition = 0

  val stateCallback: StateSaver = {
    putInt(KEY_NOW_PLAYING_POSITION, currentPosition)
  }

  val recyclerScrollListener = object : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
      super.onScrollStateChanged(recyclerView, newState)

      if (newState == RecyclerView.SCROLL_STATE_IDLE) {
        val snapView = snapHelper.findSnapView(nowPlayingPager.layoutManager)
        snapView?.let {
          val snapPosition = nowPlayingPager.getChildAdapterPosition(it)
          if (snapPosition != RecyclerView.NO_POSITION) {
            currentPosition = snapPosition
          }
        }
      }
    }
  }

  snapHelper.attachToRecyclerView(nowPlayingPager)

  nowPlayingPager.adapter = adapter
  nowPlayingPager.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

  bind {
    adapter.items = item.movies
    adapter.notifyDataSetChanged()

    currentPosition = stateSaver.state(KEY_NOW_PLAYING_POSITION, 0) as Int
    nowPlayingPager.scrollToPosition(currentPosition)
  }

  onViewAttachedToWindow {
    nowPlayingPager.addOnScrollListener(recyclerScrollListener)
    stateSaver.registerStateSaver(KEY_NOW_PLAYING_POSITION, stateCallback)

    imagesTimer = fixedRateTimer(initialDelay = TIMER_IMAGE_UPDATE, period = TIMER_IMAGE_UPDATE) {
      if (adapter.realCount == 0) {
        return@fixedRateTimer
      }
      currentPosition++
      nowPlayingPager.smoothScrollToPosition(currentPosition)
    }
  }

  onViewDetachedFromWindow {
    nowPlayingPager.removeOnScrollListener(recyclerScrollListener)
    stateSaver.unregisterStateSaver(KEY_NOW_PLAYING_POSITION)
    imagesTimer?.cancel()
  }
}
