package com.illiarb.tmdbclient.modules.video.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.databinding.WidgetYoutubePlayerBinding

class YoutubePlayer @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

  private val binding: WidgetYoutubePlayerBinding =
    WidgetYoutubePlayerBinding.bind(inflate(context, R.layout.widget_youtube_player, this))

  init {
    binding.playerView.webViewClient = object : WebViewClient() {
      override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
      ): Boolean {
        return false
      }
    }

    binding.playerView.settings.apply {
      @SuppressLint("SetJavaScriptEnabled")
      // Videos are playing only from the Youtube
      javaScriptEnabled = true
      domStorageEnabled = true
    }
  }

  fun playVideo(videoId: String) {
    binding.playerView.loadUrl(buildYoutubeUrl(videoId))
  }

  private fun buildYoutubeUrl(id: String): String = "https://www.youtube.com/embed/$id"
}