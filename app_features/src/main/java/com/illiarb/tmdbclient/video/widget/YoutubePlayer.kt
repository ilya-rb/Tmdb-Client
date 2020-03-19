package com.illiarb.tmdbclient.video.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import com.illiarb.tmdbclient.movies.home.R

class YoutubePlayer @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

  private val youtubePlayerView: WebView

  init {
    val view = inflate(context, R.layout.widget_youtube_player, this)

    youtubePlayerView = view.findViewById(R.id.playerView)
    youtubePlayerView.webViewClient = object : WebViewClient() {
      override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean = false
    }

    youtubePlayerView.settings.apply {
      @SuppressLint("SetJavaScriptEnabled")
      // Videos are playing only from the Youtube
      javaScriptEnabled = true
      domStorageEnabled = true
    }
  }

  fun playVideo(videoId: String) {
    youtubePlayerView.loadUrl(buildYoutubeUrl(videoId))
  }

  private fun buildYoutubeUrl(id: String): String {
    return "https://www.youtube.com/embed/$id"
  }
}