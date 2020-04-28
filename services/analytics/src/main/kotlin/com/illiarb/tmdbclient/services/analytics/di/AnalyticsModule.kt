package com.illiarb.tmdbclient.services.analytics.di

import android.app.Application
import com.illiarb.tmdbclient.libs.buildconfig.BuildConfig
import com.illiarb.tmdbclient.services.analytics.AnalyticsService
import com.illiarb.tmdbclient.services.analytics.internal.DefaultAnalyticsService
import com.illiarb.tmdbclient.services.analytics.internal.trackers.DebugAnalyticsTracker
import com.illiarb.tmdbclient.services.analytics.internal.trackers.FirebaseAnalyticTracker
import dagger.Module
import dagger.Provides

/**
 * @author ilya-rb on 20.02.19.
 */
@Module
object AnalyticsModule {

  @Provides
  @JvmStatic
  internal fun provideAnalyticsService(
    firebaseAnalyticTracker: FirebaseAnalyticTracker,
    debugAnalyticsTracker: DebugAnalyticsTracker,
    buildConfig: BuildConfig
  ): AnalyticsService {
    return if (buildConfig.isDebug) {
      DefaultAnalyticsService(setOf(debugAnalyticsTracker))
    } else {
      DefaultAnalyticsService(setOf(firebaseAnalyticTracker))
    }
  }

  @Provides
  @JvmStatic
  internal fun provideFirebaseTracker(app: Application): FirebaseAnalyticTracker =
    FirebaseAnalyticTracker(app)

  @Provides
  @JvmStatic
  internal fun provideDebugAnalyticsTracker(): DebugAnalyticsTracker = DebugAnalyticsTracker()
}