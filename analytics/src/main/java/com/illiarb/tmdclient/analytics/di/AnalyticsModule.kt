package com.illiarb.tmdclient.analytics.di

import com.illiarb.tmdblcient.core.analytics.AnalyticEventFactory
import com.illiarb.tmdblcient.core.analytics.AnalyticsService
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdclient.analytics.BuildConfig
import com.illiarb.tmdclient.analytics.internal.AppAnalyticsService
import com.illiarb.tmdclient.analytics.internal.DefaultAnalyticEventFactory
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
    fun provideAnalyticsEventFactory(): AnalyticEventFactory = DefaultAnalyticEventFactory()

    @Provides
    fun provideAnalyticsService(
        eventFactory: AnalyticEventFactory,
        firebaseAnalyticTracker: FirebaseAnalyticTracker,
        debugAnalyticsTracker: DebugAnalyticsTracker
    ): AnalyticsService {
        val trackers = if (BuildConfig.DEBUG) {
            setOf(debugAnalyticsTracker)
        } else {
            setOf(firebaseAnalyticTracker)
        }
        return AppAnalyticsService(eventFactory, trackers)
    }

    @Provides
    fun provideFirebaseTracker(): FirebaseAnalyticTracker = FirebaseAnalyticTracker(app)

    @Provides
    fun provideDebugAnalyticsTracker(): DebugAnalyticsTracker = DebugAnalyticsTracker()
}