package com.illiarb.tmdbclient.libs.customtabs

/**
 * Source:
 * https://github.com/saschpe/android-customtabs/blob/master/customtabs/src/main/java/saschpe/android/customtabs/CustomTabsActivityLifecycleCallbacks.kt
 */
import android.app.Activity
import android.app.Application
import android.os.Bundle

class CustomTabsActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

  private lateinit var customTabsHelper: CustomTabsHelper

  override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    customTabsHelper = CustomTabsHelper()
  }

  override fun onActivityStarted(activity: Activity) = Unit

  override fun onActivityResumed(activity: Activity) =
    customTabsHelper.bindCustomTabsService(activity)

  override fun onActivityPaused(activity: Activity) =
    customTabsHelper.unbindCustomTabsService(activity)

  override fun onActivityStopped(activity: Activity) = Unit

  override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

  override fun onActivityDestroyed(activity: Activity) = Unit
}