package com.illiarb.tmdbclient.libs.customtabs.internal

/**
 * Source:
 * https://github.com/saschpe/android-customtabs/blob/master/customtabs/src/main/java/saschpe/android/customtabs/WebViewActivity.kt
 */
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.illiarb.tmdbclient.libs.customtabs.R

internal class WebViewActivity : AppCompatActivity() {

  @SuppressLint("SetJavaScriptEnabled")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_web_view_fallback)

    val title = intent.getStringExtra(EXTRA_TITLE)
    val url = intent.getStringExtra(EXTRA_URL) ?: ""

    val actionBar = supportActionBar
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true)
      if (title != null) {
        actionBar.title = title
        actionBar.subtitle = url
      } else {
        actionBar.title = url
      }
    }

    val webView = findViewById<WebView>(R.id.web_view)
    webView.loadUrl(url)
    webView.settings.javaScriptEnabled = true

    // No title provided. Use the website's once it's loaded...
    if (title == null) {
      webView.webViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
          super.onPageFinished(view, url)
          if (actionBar != null) {
            actionBar.title = view.title
            actionBar.subtitle = url
          }
        }
      }
    }
  }

  override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
    android.R.id.home -> {
      finish()
      true
    }
    else -> super.onOptionsItemSelected(item)
  }

  companion object {
    const val EXTRA_TITLE = "EXTRA_TITLE"
    const val EXTRA_URL = "EXTRA_URL"
  }
}
