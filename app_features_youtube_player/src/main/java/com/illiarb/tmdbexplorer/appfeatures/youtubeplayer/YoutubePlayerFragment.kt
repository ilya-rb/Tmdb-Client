package com.illiarb.tmdbexplorer.appfeatures.youtubeplayer

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.illiarb.tmdbexplorer.appfeatures.youtubeplayer.databinding.FragmentYoutubePlayerBinding
import com.illiarb.tmdbexplorer.coreui.base.BaseViewBindingFragment
import com.illiarb.tmdblcient.core.navigation.Router.Action.PlayVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

@SuppressLint("SourceLockedOrientationActivity")
class YoutubePlayerFragment : BaseViewBindingFragment<FragmentYoutubePlayerBinding>() {

    private var previousOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.let {
            previousOrientation = it.requestedOrientation
            it.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycle.addObserver(binding.youtubePlayer)

        binding.youtubePlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)

                val id = requireArguments().getString(PlayVideo.EXTRA_VIDEO_ID)
                require(!id.isNullOrBlank()) {
                    "Arguments should contain valid youtube video id"
                }
                youTubePlayer.loadVideo(id, 0f)
            }
        })
    }

    override fun onDestroyView() {
        activity?.requestedOrientation = previousOrientation
        super.onDestroyView()
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentYoutubePlayerBinding =
        FragmentYoutubePlayerBinding.inflate(inflater)
}