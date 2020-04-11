package com.illiarb.tmdclient.analytics.di

import android.app.Application
import com.illiarb.tmdclient.analytics.AnalyticsService
import com.illiarb.tmdclient.analytics.BuildConfig
import com.illiarb.tmdclient.analytics.internal.DefaultAnalyticsService
import com.illiarb.tmdclient.analytics.internal.trackers.DebugAnalyticsTracker
import com.illiarb.tmdclient.analytics.internal.trackers.FirebaseAnalyticTracker
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
    debugAnalyticsTracker: DebugAnalyticsTracker
  ): AnalyticsService {
    return if (BuildConfig.DEBUG) {
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