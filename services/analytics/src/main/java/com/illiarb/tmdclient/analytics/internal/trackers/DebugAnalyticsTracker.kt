package com.illiarb.tmdclient.analytics.internal.trackers

import com.illiarb.tmdbclient.logger.Logger
import com.illiarb.tmdclient.analytics.AnalyticEvent
import com.illiarb.tmdclient.analytics.internal.AnalyticsTracker
import javax.inject.Inject

/**
 * @author ilya-rb on 20.02.19.
 */
internal class DebugAnalyticsTracker @Inject constructor() : AnalyticsTracker {

  override fun sendEvent(event: AnalyticEvent) {
    Logger.i(AnalyticsTracker.TAG, "Analytics event occurred: $event")
  }
}