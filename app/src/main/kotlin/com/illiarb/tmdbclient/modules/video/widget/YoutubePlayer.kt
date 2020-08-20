package com.illiarb.tmdbclient.modules.video.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.databinding.WidgetYoutubePlayerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class YoutubePlayer @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

  private val isPlaying: Boolean
    get() = playerState.value == PlayerConstants.PlayerState.PLAYING

  private val binding: WidgetYoutubePlayerBinding =
    WidgetYoutubePlayerBinding.bind(inflate(context, R.layout.widget_youtube_player, this))

  private val playerState = MutableStateFlow(PlayerConstants.PlayerState.UNKNOWN)

  init {
    binding.playerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
      override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
        playerState.value = state
      }
    })
  }

  fun playingStateChanges(): Flow<Boolean> =
    playerState.map { it == PlayerConstants.PlayerState.PLAYING }

  fun setLifecycleOwner(owner: LifecycleOwner) {
    owner.lifecycle.addObserver(binding.playerView)
  }

  fun playVideo(videoId: String, startSeconds: Float = 0f) {
    withYoutubePlayer { player ->
      player.loadVideo(videoId, startSeconds)
    }
  }

  fun setShowControls(show: Boolean) {
    binding.playerView.getPlayerUiController().showUi(show)
  }

  fun toggleVideo() {
    withYoutubePlayer { player ->
      if (isPlaying) {
        player.pause()
      } else {
        player.play()
      }
    }
  }

  private inline fun withYoutubePlayer(crossinline block: (YouTubePlayer) -> Unit) {
    binding.playerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
      override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
        block(youTubePlayer)
      }
    })
  }
}