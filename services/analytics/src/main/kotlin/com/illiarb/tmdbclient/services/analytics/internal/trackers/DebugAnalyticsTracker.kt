package com.illiarb.tmdbclient.services.analytics.internal.trackers

import com.illiarb.tmdbclient.libs.logger.Logger
import com.illiarb.tmdbclient.services.analytics.AnalyticEvent
import com.illiarb.tmdbclient.services.analytics.internal.AnalyticsTracker
import javax.inject.Inject

/**
 * @author ilya-rb on 20.02.19.
 */
internal class DebugAnalyticsTracker @Inject constructor() : AnalyticsTracker {

  override fun sendEvent(event: AnalyticEvent) {
    Logger.i(AnalyticsTracker.TAG, "Analytics event occurred: $event")
  }
}