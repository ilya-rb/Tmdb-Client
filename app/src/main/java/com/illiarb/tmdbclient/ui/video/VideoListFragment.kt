package com.illiarb.tmdbclient.ui.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.illiarb.tmdbclient.databinding.FragmentVideoListBinding
import com.illiarb.tmdbclient.di.AppComponent
import com.illiarb.tmdbclient.di.Injectable
import com.illiarb.tmdbclient.navigation.Router
import com.illiarb.tmdbclient.ui.video.VideoListViewModel.Event
import com.illiarb.tmdbclient.ui.video.VideoListViewModel.State
import com.illiarb.tmdbclient.ui.video.di.DaggerVideoListComponent
import com.illiarb.tmdbexplorer.coreui.base.BaseViewBindingFragment
import com.illiarb.tmdbexplorer.coreui.ext.doOnApplyWindowInsets
import com.illiarb.tmdbexplorer.coreui.ext.removeAdapterOnDetach
import com.illiarb.tmdbexplorer.coreui.ext.updatePadding
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.DelegatesAdapter
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

    binding.toolbar.setNavigationOnClickListener {
      requireActivity().onBackPressed()
    }

    bind()

    ViewCompat.requestApplyInsets(view)
  }

  override fun inject(appComponent: AppComponent) =
    DaggerVideoListComponent.builder()
      .dependencies(appComponent)
      .movieId(requireArguments().getInt(Router.Action.ShowVideos.EXTRA_MOVIE_ID))
      .build()
      .inject(this)

  override fun getViewBinding(inflater: LayoutInflater): FragmentVideoListBinding =
    FragmentVideoListBinding.inflate(inflater)

  private fun setupVideoPlayer() {
    binding.youtubePlayer.doOnApplyWindowInsets { view, windowInsets, _ ->
      view.updatePadding(top = windowInsets.systemWindowInsetTop)
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
      addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
          binding.appBar.setLifted(dy > 0)
        }
      })
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