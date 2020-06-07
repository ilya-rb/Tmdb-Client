package com.illiarb.tmdbclient

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import com.illiarb.tmdbclient.libs.buildconfig.BuildConfig
import com.illiarb.tmdbclient.libs.buildconfig.TmdbConfig
import com.illiarb.tmdbclient.BuildConfig as AndroidBuildConfig

class AppBuildConfig(private val context: Context) : BuildConfig {

  override val isDebug: Boolean get() = AndroidBuildConfig.DEBUG
  override val sdkInt: Int get() = Build.VERSION.SDK_INT
  override val applicationId: String get() = AndroidBuildConfig.APPLICATION_ID
  override val buildType: String get() = AndroidBuildConfig.BUILD_TYPE
  override val isQ: Boolean get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

  override val versionCode: Long
    @SuppressLint("NewApi")
    @Suppress("DEPRECATION")
    get() {
      val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)

      return if (sdkInt >= Build.VERSION_CODES.P) {
        packageInfo.longVersionCode
      } else {
        packageInfo.versionCode.toLong()
      }
    }

  override val versionName: String
    get() = context.packageManager.getPackageInfo(context.packageName, 0).versionName
}

class AppTmdbConfig : TmdbConfig {
  override val apiKey: String get() = AndroidBuildConfig.API_KEY
  override val apiUrl: String get() = AndroidBuildConfig.API_URL
}