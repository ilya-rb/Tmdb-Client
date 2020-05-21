package com.illiarb.tmdbclient.libs.customtabs.internal

/**
 * Source:
 * https://github.com/saschpe/android-customtabs/blob/master/customtabs/src/main/java/saschpe/android/customtabs/KeepAliveService.kt
 */
import android.app.Service
import android.content.Intent
import android.os.Binder

/**
 * Empty service to bind to, raising the application's importance.
 */
internal class KeepAliveService : Service() {

  override fun onBind(intent: Intent) = binder

  companion object {
    private val binder = Binder()
  }
}