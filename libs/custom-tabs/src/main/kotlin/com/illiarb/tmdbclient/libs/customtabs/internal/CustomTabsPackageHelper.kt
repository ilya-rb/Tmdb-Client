package com.illiarb.tmdbclient.libs.customtabs.internal

/**
 * Source:
 * https://github.com/saschpe/android-customtabs/blob/master/customtabs/src/main/java/saschpe/android/customtabs/CustomTabsPackageHelper.kt
 */
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.browser.customtabs.CustomTabsService

/**
 * Helper class for Custom Tabs.
 */
internal object CustomTabsPackageHelper {

  private const val STABLE_PACKAGE = "com.android.chrome"

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
  @Suppress("ComplexMethod")
  fun getPackageNameToUse(context: Context): String? {
    if (packageNameToUse != null) {
      return packageNameToUse
    }

    val pm = context.packageManager

    // Get default VIEW intent handler.
    val activityIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"))
    val defaultHandlerInfo = pm.resolveActivity(activityIntent, 0)
    val defaultHandlerPackageName: String? = defaultHandlerInfo?.activityInfo?.packageName
    val supportedApps = pm.getSupportedAppsForIntent(activityIntent)

    // Now packagesSupportingCustomTabs contains all apps that can handle both VIEW intents
    // and service calls.
    packageNameToUse = when {
      supportedApps.isEmpty() -> null
      supportedApps.size == 1 -> supportedApps[0]
      !defaultHandlerPackageName.isNullOrEmpty()
        && !defaultHandlerPackageName.isNullOrEmpty()
        && !hasSpecializedHandlerIntents(context, activityIntent)
        && supportedApps.contains(defaultHandlerPackageName) -> defaultHandlerPackageName
      supportedApps.contains(STABLE_PACKAGE) -> STABLE_PACKAGE
      else -> null
    }
    return packageNameToUse
  }

  // Get all apps that can handle VIEW intents.
  private fun PackageManager.getSupportedAppsForIntent(intent: Intent): List<String> {
    val resolvedActivityList = queryIntentActivities(intent, 0)
    val packagesSupportedIntent = mutableListOf<String>()

    resolvedActivityList.forEach { info ->
      val serviceIntent = Intent().apply {
        action = CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION
        setPackage(info.activityInfo.packageName)
      }

      if (resolveService(serviceIntent, 0) != null) {
        packagesSupportedIntent.add(info.activityInfo.packageName)
      }
    }

    return packagesSupportedIntent
  }

  /**
   * Used to check whether there is a specialized handler for a given intent.
   *
   * @param intent The intent to check with.
   * @return Whether there is a specialized handler for the given intent.
   */
  private fun hasSpecializedHandlerIntents(context: Context, intent: Intent): Boolean {
    val pm = context.packageManager
    val handlers = pm.queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER)

    return handlers.any { resolveInfo ->
      resolveInfo?.filter?.countDataAuthorities() == 0 || resolveInfo?.filter?.countDataPaths() == 0
    }
  }
}