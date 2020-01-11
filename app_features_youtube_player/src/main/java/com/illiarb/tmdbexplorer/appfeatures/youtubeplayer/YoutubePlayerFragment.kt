package com.illiarb.tmdbexplorer.appfeatures.youtubeplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.illiarb.tmdbexplorer.appfeatures.youtubeplayer.YoutubePlayerModel.UiEvent
import com.illiarb.tmdbexplorer.appfeatures.youtubeplayer.databinding.FragmentYoutubePlayerBinding
import com.illiarb.tmdbexplorer.appfeatures.youtubeplayer.di.YoutubePlayerComponent
import com.illiarb.tmdbexplorer.coreui.base.BaseViewBindingFragment
import com.illiarb.tmdbexplorer.coreui.ext.doOnApplyWindowInsets
import com.illiarb.tmdbexplorer.coreui.ext.removeAdapterOnDetach
import com.illiarb.tmdbexplorer.coreui.ext.updateMargin
import com.illiarb.tmdbexplorer.coreui.ext.updatePadding
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.DelegatesAdapter
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowVideos
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class YoutubePlayerFragment : BaseViewBindingFragment<FragmentYoutubePlayerBinding>(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val videosAdapter = DelegatesAdapter({ listOf(videoDelegate(it)) })

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory).get(DefaultYoutubePlayerModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupVideoPlayer()
        setupVideoList()

        lifecycleScope.launch {
            videosAdapter.clicks().collect {
                viewModel.onUiEvent(UiEvent.ItemClick(it))
            }
        }

        bind()

        ViewCompat.requestApplyInsets(view)
    }

    override fun inject(appProvider: AppProvider) {
        val movieId = requireArguments().getInt(ShowVideos.EXTRA_MOVIE_ID)
        require(movieId != 0) {
            "Arguments must contain valid movie id"
        }
        YoutubePlayerComponent.get(appProvider, movieId).inject(this)
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentYoutubePlayerBinding =
        FragmentYoutubePlayerBinding.inflate(inflater)

    private fun setupVideoPlayer() {
        viewLifecycleOwner.lifecycle.addObserver(binding.youtubePlayer)

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
        viewModel.videos.observe(viewLifecycleOwner, videosAdapter)
        viewModel.selectedVideo.observe(viewLifecycleOwner, Observer {
            playVideo(it.video.key)
        })
    }

    private fun playVideo(videoId: String) {
        binding.youtubePlayer.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(videoId, 0f)
            }
        })
    }
}