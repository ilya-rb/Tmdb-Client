package com.illiarb.tmdclient.analytics.di

import com.illiarb.tmdblcient.core.analytics.AnalyticsService
import com.illiarb.tmdblcient.core.app.App
import com.illiarb.tmdclient.analytics.BuildConfig
import com.illiarb.tmdclient.analytics.DefaultAnalyticsService
import com.illiarb.tmdclient.analytics.trackers.DebugAnalyticsTracker
import com.illiarb.tmdclient.analytics.trackers.FirebaseAnalyticTracker
import dagger.Module
import dagger.Provides

/**
 * @author ilya-rb on 20.02.19.
 */
@Module
class AnalyticsModule(private val app: App) {

  @Provides
  fun provideAnalyticsService(
    firebaseAnalyticTracker: FirebaseAnalyticTracker,
    debugAnalyticsTracker: DebugAnalyticsTracker
  ): AnalyticsService {
    val trackers = if (BuildConfig.DEBUG) {
      setOf(debugAnalyticsTracker)
    } else {
      setOf(firebaseAnalyticTracker)
    }
    return DefaultAnalyticsService(trackers)
  }

  @Provides
  fun provideFirebaseTracker(): FirebaseAnalyticTracker = FirebaseAnalyticTracker(app)

  @Provides
  fun provideDebugAnalyticsTracker(): DebugAnalyticsTracker = DebugAnalyticsTracker()
}