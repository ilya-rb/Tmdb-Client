package com.illiarb.tmdbclient.initializers

import android.app.Application
import android.os.StrictMode
import com.illiarb.tmdbclient.libs.buildconfig.BuildConfig
import com.illiarb.tmdbclient.libs.tools.AppInitializer
import javax.inject.Inject

class StrictModeInitializer @Inject constructor(
  private val buildConfig: BuildConfig
) : AppInitializer {

  override fun initialize(app: Application) {
    // TODO: Maybe move this into debug source set
    if (buildConfig.isDebug) return

    StrictMode.setThreadPolicy(
      StrictMode.ThreadPolicy.Builder()
        .detectDiskWrites()
        .detectDiskReads()
        .detectNetwork()
        .penaltyLog()
        .build()
    )

    StrictMode.setVmPolicy(
      StrictMode.VmPolicy.Builder()
        .detectLeakedSqlLiteObjects()
        .detectLeakedClosableObjects()
        .detectActivityLeaks()
        .penaltyLog()
        .penaltyDeath()
        .build()
    )
  }
}