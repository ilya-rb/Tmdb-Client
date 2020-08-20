package com.illiarb.tmdbclient.modules.video

import android.graphics.Color
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.transition.MaterialSharedAxis
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.databinding.FragmentVideoListBinding
import com.illiarb.tmdbclient.di.AppProvider
import com.illiarb.tmdbclient.di.Injectable
import com.illiarb.tmdbclient.libs.ui.base.BaseFragment
import com.illiarb.tmdbclient.libs.ui.common.TranslucentStatusBarColorChanger
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

  private val viewModel by viewModels<VideoListViewModel>(factoryProducer = { viewModelFactory })
  private val viewBinding by viewBinding { fragment ->
    FragmentVideoListBinding.bind(fragment.requireView())
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    exitTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewBinding.root.setSkipTouchEventOnState(
      R.id.end,
      skip = true,
      excludeViewIds = listOf(
        R.id.videoListClose,
        R.id.videoListPlay,
        R.id.videoListCollapsedBackground,
        R.id.youtubePlayer
      )
    )

    viewBinding.videoListClose.setOnClickListener {
      viewModel.events.offer(Event.CloseClicked)
    }

    viewBinding.videoListPlay.setOnClickListener {
      viewBinding.youtubePlayer.toggleVideo()
    }

    setupVideoPlayer()

    setupVideoList()

    TranslucentStatusBarColorChanger(this, requireActivity().window, Color.BLACK)

    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, enabled = true) {
      if (viewBinding.root.currentState == R.id.start) {
        viewBinding.root.transitionToEnd()
      } else {
        viewModel.events.offer(Event.CloseClicked)
      }
    }

    viewLifecycleOwner.lifecycleScope.launch {
      viewBinding.youtubePlayer.playingStateChanges().collect { isPlaying ->
        val icon = viewBinding.videoListPlay.drawable as? AnimatedVectorDrawable
        icon?.let {
          if (isPlaying) {
            it.start()
          } else {
            it.reset()
          }
        }
      }
    }

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
    viewBinding.youtubePlayer.setLifecycleOwner(viewLifecycleOwner)
    viewBinding.youtubePlayer.doOnApplyWindowInsets { view, windowInsets, _ ->
      view.updatePadding(top = windowInsets.systemWindowInsetTop)
    }

    viewBinding.videoListRoot.setTransitionListener(object : MotionLayout.TransitionListener {
      override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) = Unit
      override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) = Unit
      override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) = Unit
      override fun onTransitionCompleted(motionLayout: MotionLayout?, state: Int) {
        viewBinding.youtubePlayer.setShowControls(state == R.id.start)
      }
    })
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

    newModel.selected?.let { selected ->
      viewBinding.videoListTitle.text = selected.video.name
    }

    if (oldModel?.selected != newModel.selected && newModel.selected != null) {
      playVideo(newModel.selected.video.key)
    }
  }

  private fun playVideo(videoId: String) {
    viewBinding.youtubePlayer.playVideo(videoId)
  }
}