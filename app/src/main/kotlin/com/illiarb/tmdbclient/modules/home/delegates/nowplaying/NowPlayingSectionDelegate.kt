package com.illiarb.tmdbclient.modules.home.delegates.nowplaying

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.illiarb.tmdbclient.databinding.ItemNowPlayingSectionBinding
import com.illiarb.tmdbclient.libs.ui.common.OnClickListener
import com.illiarb.tmdbclient.libs.ui.common.SimpleBundleStore
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.domain.MovieSection
import com.illiarb.tmdbclient.services.tmdb.domain.NowPlayingSection

// 10 seconds
private const val PAGE_UPDATE_INTERVAL = 10000
private const val KEY_NOW_PLAYING_STATE = "now_playing_state"
private const val KEY_NOW_PLAYING_PROGRESS = "now_playing_progress"

fun nowPlayingSection(
  bundleStore: SimpleBundleStore,
  clickListener: OnClickListener<Movie>
) = adapterDelegateViewBinding<NowPlayingSection, MovieSection, ItemNowPlayingSectionBinding>(
  { inflater, root -> ItemNowPlayingSectionBinding.inflate(inflater, root, false) }
) {

  val snapHelper = PagerSnapHelper()
  val adapter = NowPlayingPagerAdapter(clickListener).apply {
    stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
  }

  var currentPosition = 0
  val progressUpdateTimer = ProgressUpdateTimer(
    context = binding.root.context,
    max = PAGE_UPDATE_INTERVAL
  ) {
    binding.nowPlayingProgress.incrementProgressBy(it)

    if (binding.nowPlayingProgress.progress == binding.nowPlayingProgress.max) {
      if (adapter.realCount == 0) {
        return@ProgressUpdateTimer
      }
      cancelTimer()
      currentPosition++
      binding.nowPlayingPager.smoothScrollToPosition(currentPosition)
    }
  }

  val recyclerScrollListener = object : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
      super.onScrollStateChanged(recyclerView, newState)

      if (newState == RecyclerView.SCROLL_STATE_IDLE) {
        val snapView = snapHelper.findSnapView(binding.nowPlayingPager.layoutManager)
        snapView?.let {
          val snapPosition = binding.nowPlayingPager.getChildAdapterPosition(it)
          if (snapPosition != RecyclerView.NO_POSITION) {
            currentPosition = snapPosition
            binding.nowPlayingProgress.progress = 0
            progressUpdateTimer.resetTimer()
          }
        }
      }
    }
  }

  snapHelper.attachToRecyclerView(binding.nowPlayingPager)

  binding.nowPlayingProgress.max = PAGE_UPDATE_INTERVAL
  binding.nowPlayingPager.adapter = adapter
  binding.nowPlayingPager.layoutManager =
    LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

  bind {
    adapter.items = item.movies
    adapter.notifyDataSetChanged()

    binding.nowPlayingProgress.progress = bundleStore.getInt(KEY_NOW_PLAYING_PROGRESS)
    binding.nowPlayingPager.layoutManager?.onRestoreInstanceState(
      bundleStore.getParcelable(KEY_NOW_PLAYING_STATE)
    )
  }

  onViewAttachedToWindow {
    binding.nowPlayingPager.addOnScrollListener(recyclerScrollListener)
    progressUpdateTimer.resetTimer()
  }

  onViewDetachedFromWindow {
    binding.nowPlayingPager.removeOnScrollListener(recyclerScrollListener)

    progressUpdateTimer.cancelTimer()

    bundleStore.putInt(KEY_NOW_PLAYING_PROGRESS, binding.nowPlayingProgress.progress)
    bundleStore.putParcelable(
      KEY_NOW_PLAYING_STATE,
      binding.nowPlayingPager.layoutManager?.onSaveInstanceState()
    )
  }
}
