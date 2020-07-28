package com.illiarb.tmdbclient.libs.customtabs

/**
 * Source:
 * https://github.com/saschpe/android-customtabs/blob/master/customtabs/src/main/java/saschpe/android/customtabs/CustomTabsHelper.kt
 */
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.browser.customtabs.CustomTabsIntent
import com.illiarb.tmdbclient.libs.customtabs.internal.CustomTabsPackageHelper

object CustomTabsHelper {

  /**
   * Opens the URL on a Custom Tab if possible. Otherwise fallsback to opening it on a WebView
   *
   * @param context The host activity
   * @param customTabsIntent a CustomTabsIntent to be used if Custom Tabs is available
   * @param uri the Uri to be opened
   * @param fallback a CustomTabFallback to be used if Custom Tabs is not available
   */
  fun openCustomTab(
    context: Context,
    customTabsIntent: CustomTabsIntent,
    uri: Uri,
    fallback: CustomTabFallback?
  ) {
    val packageName = CustomTabsPackageHelper.getPackageNameToUse(context)
    // If we cant find a package name, it means there's no browser that supports
    // Chrome Custom Tabs installed. So, we fallback to the web-view
    if (packageName == null) {
      fallback?.openUri(context, uri)
    } else {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
        customTabsIntent.intent
          .putExtra(
            Intent.EXTRA_REFERRER,
            Uri.parse("${Intent.URI_ANDROID_APP_SCHEME}//${context.packageName}")
          )
      }
      customTabsIntent.intent.setPackage(packageName)
      customTabsIntent.launchUrl(context, uri)
    }
  }
}