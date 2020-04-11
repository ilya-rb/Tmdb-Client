package com.illiarb.tmdbclient.services.analytics.internal

import com.illiarb.tmdbclient.services.analytics.AnalyticEvent

/**
 * @author ilya-rb on 19.02.19.
 */
interface AnalyticsTracker {

  companion object {
    const val TAG = "AnalyticsTracker"
  }

  fun sendEvent(event: AnalyticEvent)

}