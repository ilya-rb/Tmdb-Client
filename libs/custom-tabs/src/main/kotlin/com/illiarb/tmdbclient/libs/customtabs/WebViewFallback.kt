package com.illiarb.tmdbclient.libs.customtabs

/**
 * Source:
 * https://github.com/saschpe/android-customtabs/blob/master/customtabs/src/main/java/saschpe/android/customtabs/WebViewFallback.kt
 */
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.illiarb.tmdbclient.libs.customtabs.internal.WebViewActivity

/**
 * Default [CustomTabsHelper.CustomTabFallback] implementation
 * that uses [WebViewActivity] to display the requested [Uri].
 */
class WebViewFallback : CustomTabsHelper.CustomTabFallback {
  /**
   * @param context The [Context] that wants to open the Uri
   * @param uri The [Uri] to be opened by the fallback
   */
  override fun openUri(context: Context?, uri: Uri?) {
    context?.startActivity(
      Intent(context, WebViewActivity::class.java)
        .putExtra(WebViewActivity.EXTRA_URL, uri.toString())
    )
  }
}