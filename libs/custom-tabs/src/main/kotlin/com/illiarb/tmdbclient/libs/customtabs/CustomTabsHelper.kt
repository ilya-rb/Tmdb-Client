package com.illiarb.tmdbclient.libs.customtabs

/**
 * Source:
 * https://github.com/saschpe/android-customtabs/blob/master/customtabs/src/main/java/saschpe/android/customtabs/CustomTabsHelper.kt
 */
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import com.illiarb.tmdbclient.libs.customtabs.internal.CustomTabsPackageHelper
import com.illiarb.tmdbclient.libs.customtabs.internal.KeepAliveService
import com.illiarb.tmdbclient.libs.logger.Logger

class CustomTabsHelper {

  companion object {

    private const val TAG = "CustomTabsHelper"
    private const val EXTRA_CUSTOM_TABS_KEEP_ALIVE =
      "android.support.customtabs.extra.KEEP_ALIVE"

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

    fun addKeepAliveExtra(context: Context, intent: Intent) {
      intent.putExtra(EXTRA_CUSTOM_TABS_KEEP_ALIVE, Intent().apply {
        setClassName(context.packageName, KeepAliveService::class.java.canonicalName as String)
      })
    }
  }

  private var customTabsSession: CustomTabsSession? = null
  private var client: CustomTabsClient? = null
  private var connection: CustomTabsServiceConnection? = null
  private var connectionCallback: ConnectionCallback? = null

  /**
   * Unbinds the Activity from the Custom Tabs Service
   *
   * @param activity the activity that is connected to the service
   */
  fun unbindCustomTabsService(activity: Activity) {
    connection?.let {
      try {
        activity.unbindService(it)
      } catch (e: IllegalArgumentException) {
        Logger.e(TAG, "Error unbinding service", e)
      }
      client = null
      customTabsSession = null
    }
  }

  /**
   * Creates or retrieves an exiting CustomTabsSession
   *
   * @return a CustomTabsSession
   */
  val session: CustomTabsSession?
    get() {
      when {
        client == null -> customTabsSession = null
        customTabsSession == null -> customTabsSession = client?.newSession(null)
      }
      return customTabsSession
    }

  /**
   * Register a Callback to be called when connected or disconnected from the Custom Tabs Service
   */
  fun setConnectionCallback(callback: ConnectionCallback?) {
    connectionCallback = callback
  }

  /**
   * Binds the Activity to the Custom Tabs Service
   *
   * @param activity the activity to be bound to the service
   */
  fun bindCustomTabsService(activity: Activity?) {
    if (client != null) {
      return
    }
    val packageName = CustomTabsPackageHelper.getPackageNameToUse(activity!!) ?: return
    val myConnection = object : CustomTabsServiceConnection() {
      override fun onCustomTabsServiceConnected(
        name: ComponentName,
        newClient: CustomTabsClient
      ) {
        client = newClient
        client?.warmup(0L)
        connectionCallback?.onCustomTabsConnected()
        // Initialize a session as soon as possible.
        session
      }

      override fun onServiceDisconnected(name: ComponentName) {
        client = null
        connectionCallback?.onCustomTabsDisconnected()
      }

      override fun onBindingDied(name: ComponentName) {
        client = null
        connectionCallback?.onCustomTabsDisconnected()
      }
    }
    connection = myConnection
    CustomTabsClient.bindCustomTabsService(activity, packageName, myConnection)
  }

  fun mayLaunchUrl(uri: Uri, extras: Bundle?, otherLikelyBundles: List<Bundle?>?) =
    when (client) {
      null -> false
      else -> session?.mayLaunchUrl(uri, extras, otherLikelyBundles) ?: false
    }

  /**
   * A Callback for when the service is connected or disconnected. Use those callbacks to
   * handle UI changes when the service is connected or disconnected
   */
  interface ConnectionCallback {
    /**
     * Called when the service is connected
     */
    fun onCustomTabsConnected()

    /**
     * Called when the service is disconnected
     */
    fun onCustomTabsDisconnected()
  }

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
}