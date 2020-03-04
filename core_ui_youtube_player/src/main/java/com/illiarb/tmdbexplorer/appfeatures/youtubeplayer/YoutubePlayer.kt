package com.illiarb.tmdbexplorer.appfeatures.youtubeplayer

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class YoutubePlayer @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

  private var stateListener: StateListener? = null
  private val youtubePlayerView: YouTubePlayerView

  init {
    val view = inflate(context, R.layout.widget_youtube_player, this)

    youtubePlayerView = view.findViewById(R.id.playerView)
    youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
      override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
        if (state == PlayerConstants.PlayerState.ENDED) {
          stateListener?.onVideoEnded()
        }
      }
    })
  }

  fun bindToLifecycle(owner: LifecycleOwner) {
    owner.lifecycle.addObserver(youtubePlayerView)
  }

  fun playVideo(videoId: String, position: Float = 0f) {
    youtubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
      override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
        youTubePlayer.loadVideo(videoId, position)
      }
    })
  }

  fun setPlayerStateListener(stateListener: StateListener) {
    this.stateListener = stateListener
  }

  interface StateListener {

    fun onVideoEnded()
  }
}