package com.illiarb.tmdbclient.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.illiarb.tmdbclient.movies.home.databinding.FragmentVideoListBinding
import com.illiarb.tmdbclient.video.VideoListViewModel.Event
import com.illiarb.tmdbclient.video.VideoListViewModel.State
import com.illiarb.tmdbclient.video.di.VideoListComponent
import com.illiarb.tmdbexplorer.appfeatures.youtubeplayer.YoutubePlayer
import com.illiarb.tmdbexplorer.coreui.base.BaseViewBindingFragment
import com.illiarb.tmdbexplorer.coreui.ext.doOnApplyWindowInsets
import com.illiarb.tmdbexplorer.coreui.ext.removeAdapterOnDetach
import com.illiarb.tmdbexplorer.coreui.ext.updateMargin
import com.illiarb.tmdbexplorer.coreui.ext.updatePadding
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.DelegatesAdapter
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.navigation.Router
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoListFragment : BaseViewBindingFragment<FragmentVideoListBinding>(), Injectable {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  private val videosAdapter = DelegatesAdapter(
    videoTypeDelegate(),
    videoDelegate {
      viewModel.events.offer(Event.VideoClicked(it))
    }
  )

  private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
    ViewModelProvider(this, viewModelFactory).get(VideoListViewModel::class.java)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setupVideoPlayer()
    setupVideoList()

    bind()

    ViewCompat.requestApplyInsets(view)
  }

  override fun inject(appProvider: AppProvider) {
    val movieId = requireArguments().getInt(Router.Action.ShowVideos.EXTRA_MOVIE_ID)
    require(movieId != 0) {
      "Arguments must contain valid movie id"
    }
    VideoListComponent.get(appProvider, movieId).inject(this)
  }

  override fun getViewBinding(inflater: LayoutInflater): FragmentVideoListBinding =
    FragmentVideoListBinding.inflate(inflater)

  private fun setupVideoPlayer() {
    binding.youtubePlayer.bindToLifecycle(viewLifecycleOwner)
    binding.youtubePlayer.setPlayerStateListener(object : YoutubePlayer.StateListener {
      override fun onVideoEnded() {
        viewModel.events.offer(Event.VideoEnded)
      }
    })

    binding.youtubePlayer.doOnApplyWindowInsets { view, windowInsets, _ ->
      view.updateMargin(top = windowInsets.systemWindowInsetTop)
    }
  }

  private fun setupVideoList() {
    binding.youtubeVideosList.apply {
      adapter = videosAdapter
      layoutManager = LinearLayoutManager(requireContext())
      removeAdapterOnDetach()
      doOnApplyWindowInsets { view, windowInsets, padding ->
        view.updatePadding(bottom = padding.bottom + windowInsets.systemWindowInsetBottom)
      }
    }
  }

  private fun bind() {
    viewLifecycleOwner.lifecycleScope.launch {
      var oldModel: State? = null

      viewModel.state.collect { newState ->
        render(oldModel, newState)
        oldModel = newState
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
    binding.youtubePlayer.playVideo(videoId)
  }
}