package com.illiarb.tmdbclient.modules.video

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.databinding.FragmentVideoListBinding
import com.illiarb.tmdbclient.di.AppProvider
import com.illiarb.tmdbclient.di.Injectable
import com.illiarb.tmdbclient.libs.ui.base.BaseFragment
import com.illiarb.tmdbclient.libs.ui.ext.doOnApplyWindowInsets
import com.illiarb.tmdbclient.libs.ui.ext.removeAdapterOnDetach
import com.illiarb.tmdbclient.libs.ui.ext.updatePadding
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.DelegatesAdapter
import com.illiarb.tmdbclient.modules.video.VideoListViewModel.Event
import com.illiarb.tmdbclient.modules.video.VideoListViewModel.State
import com.illiarb.tmdbclient.modules.video.di.DaggerVideoListComponent
import com.illiarb.tmdbclient.navigation.NavigationAction
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoListFragment : BaseFragment(R.layout.fragment_video_list), Injectable {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  private val videosAdapter = DelegatesAdapter(
    videoTypeDelegate(),
    videoDelegate {
      viewModel.events.offer(Event.VideoClicked(it))
    }
  )

  private val viewBinding by viewBinding { fragment ->
    FragmentVideoListBinding.bind(fragment.requireView())
  }

  private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
    ViewModelProvider(this, viewModelFactory).get(VideoListViewModel::class.java)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setupVideoPlayer()
    setupVideoList()

    viewLifecycleOwner.lifecycleScope.launch {
      var oldModel: State? = null

      viewModel.state.collect { newState ->
        render(oldModel, newState)
        oldModel = newState
      }
    }

    ViewCompat.requestApplyInsets(view)
  }

  override fun inject(appProvider: AppProvider) =
    DaggerVideoListComponent.factory()
      .create(
        movieId = requireArguments().getInt(NavigationAction.EXTRA_VIDEOS_MOVIE_ID),
        dependencies = appProvider
      )
      .inject(this)

  private fun setupVideoPlayer() {
    viewBinding.youtubePlayer.doOnApplyWindowInsets { view, windowInsets, _ ->
      view.updatePadding(top = windowInsets.systemWindowInsetTop)
    }
  }

  private fun setupVideoList() {
    viewBinding.youtubeVideosList.apply {
      adapter = videosAdapter
      layoutManager = LinearLayoutManager(requireContext())
      setHasFixedSize(true)
      itemAnimator?.changeDuration = 0L
      removeAdapterOnDetach()
      doOnApplyWindowInsets { view, windowInsets, padding ->
        view.updatePadding(bottom = padding.bottom + windowInsets.systemWindowInsetBottom)
      }
    }
  }

  private fun render(oldModel: State?, newModel: State) {
    videosAdapter.submitList(newModel.videos)

    if (oldModel?.selected != newModel.selected && newModel.selected != null) {
      playVideo(newModel.selected.video.key)
    }
  }

  private fun playVideo(videoId: String) {
    viewBinding.youtubePlayer.playVideo(videoId)
  }
}