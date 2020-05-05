package com.illiarb.tmdbclient.libs.customtabs.internal

/**
 * Source:
 * https://github.com/saschpe/android-customtabs/blob/master/customtabs/src/main/java/saschpe/android/customtabs/CustomTabsPackageHelper.kt
 */
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import androidx.browser.customtabs.CustomTabsService
import com.illiarb.tmdbclient.libs.logger.Logger

/**
 * Helper class for Custom Tabs.
 */
internal object CustomTabsPackageHelper {

  private const val STABLE_PACKAGE = "com.android.chrome"

  private val TAG = CustomTabsPackageHelper::class.java.simpleName
  private var packageNameToUse: String? = null

  /**
   * Goes through all apps that handle VIEW intents and have a warmup service. Picks
   * the one chosen by the user if there is one, otherwise makes a best effort to return a
   * valid package name.
   *
   * This is **not** threadsafe.
   *
   * @param context [Context] to use for accessing [PackageManager].
   * @return The package name recommended to use for connecting to custom tabs related components.
   */
  @JvmStatic
  fun getPackageNameToUse(context: Context): String? {
    if (packageNameToUse != null) {
      return packageNameToUse
    }

    val pm = context.packageManager

    // Get default VIEW intent handler.
    val activityIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"))
    val defaultHandlerInfo = pm.resolveActivity(activityIntent, 0)
    val defaultHandlerPackageName: String? = defaultHandlerInfo?.activityInfo?.packageName

    // Get all apps that can handle VIEW intents.
    val resolvedActivityList = pm.queryIntentActivities(activityIntent, 0)
    val packagesSupportingCustomTabs = mutableListOf<String>()
    resolvedActivityList.forEach { info ->
      val serviceIntent = Intent().apply {
        action = CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION
        setPackage(info.activityInfo.packageName)
      }

      if (pm.resolveService(serviceIntent, 0) != null) {
        packagesSupportingCustomTabs.add(info.activityInfo.packageName)
      }
    }

    // Now packagesSupportingCustomTabs contains all apps that can handle both VIEW intents
    // and service calls.
    packageNameToUse = when {
      packagesSupportingCustomTabs.isEmpty() -> null
      packagesSupportingCustomTabs.size == 1 -> packagesSupportingCustomTabs[0]
      !defaultHandlerPackageName.isNullOrEmpty()
          && !TextUtils.isEmpty(defaultHandlerPackageName)
          && !hasSpecializedHandlerIntents(context, activityIntent)
          && packagesSupportingCustomTabs.contains(defaultHandlerPackageName) -> defaultHandlerPackageName
      packagesSupportingCustomTabs.contains(STABLE_PACKAGE) -> STABLE_PACKAGE
      else -> null
    }
    return packageNameToUse
  }

  /**
   * Used to check whether there is a specialized handler for a given intent.
   *
   * @param intent The intent to check with.
   * @return Whether there is a specialized handler for the given intent.
   */
  private fun hasSpecializedHandlerIntents(context: Context, intent: Intent): Boolean {
    try {
      val pm = context.packageManager
      val handlers = pm.queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER)
      if (handlers.size == 0) {
        return false
      }
      handlers.forEach { resolveInfo ->
        val filter = resolveInfo.filter ?: return@forEach
        if (filter.countDataAuthorities() == 0 || filter.countDataPaths() == 0) return@forEach
        if (resolveInfo.activityInfo == null) return@forEach
        return true
      }
    } catch (e: RuntimeException) {
      Logger.e(TAG, "Runtime exception while getting specialized handlers")
    }
    return false
  }
}