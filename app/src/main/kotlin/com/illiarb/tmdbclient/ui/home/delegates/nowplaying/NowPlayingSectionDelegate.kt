package com.illiarb.tmdbclient.ui.home.delegates.nowplaying

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.libs.ui.common.OnClickListener
import com.illiarb.tmdbclient.libs.ui.common.SimpleBundleStore
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.domain.MovieSection
import com.illiarb.tmdbclient.services.tmdb.domain.NowPlayingSection
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

// 10 seconds
private const val IMAGE_CHANGE_UPDATE_INTERVAL = 10000L
private const val KEY_NOW_PLAYING_STATE = "now_playing_state"

fun nowPlayingSection(
  bundleStore: SimpleBundleStore,
  clickListener: OnClickListener<Movie>
) = adapterDelegate<NowPlayingSection, MovieSection>(R.layout.item_now_playing_section) {

  val nowPlayingPager = itemView.findViewById<RecyclerView>(R.id.nowPlayingPager)
  val snapHelper = PagerSnapHelper()
  val adapter = NowPlayingPagerAdapter(clickListener).apply {
    stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
  }

  var imagesTimer: Timer? = null
  var currentPosition = 0

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
  nowPlayingPager.layoutManager =
    LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

  bind {
    adapter.items = item.movies
    adapter.notifyDataSetChanged()

    nowPlayingPager.layoutManager?.onRestoreInstanceState(
      bundleStore.getParcelable(KEY_NOW_PLAYING_STATE)
    )
  }

  onViewAttachedToWindow {
    nowPlayingPager.addOnScrollListener(recyclerScrollListener)

    imagesTimer = fixedRateTimer(
      initialDelay = IMAGE_CHANGE_UPDATE_INTERVAL,
      period = IMAGE_CHANGE_UPDATE_INTERVAL
    ) {
      if (adapter.realCount == 0) {
        return@fixedRateTimer
      }
      currentPosition++
      nowPlayingPager.smoothScrollToPosition(currentPosition)
    }
  }

  onViewDetachedFromWindow {
    nowPlayingPager.removeOnScrollListener(recyclerScrollListener)
    imagesTimer?.cancel()
    bundleStore.putParcelable(
      KEY_NOW_PLAYING_STATE,
      nowPlayingPager.layoutManager?.onSaveInstanceState()
    )
  }
}
