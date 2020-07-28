package com.illiarb.tmdbclient.libs.customtabs

import android.content.Context
import android.net.Uri

/**
 * To be used as a fallback to open the Uri when Custom Tabs is not available
 */
interface CustomTabFallback {
  /**
   * @param context The Activity that wants to open the Uri
   * @param uri The uri to be opened by the fallback
   */
  fun openUri(context: Context?, uri: Uri?)
}