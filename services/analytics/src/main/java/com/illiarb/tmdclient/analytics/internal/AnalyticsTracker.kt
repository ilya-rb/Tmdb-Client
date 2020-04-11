package com.illiarb.tmdclient.analytics.internal

import com.illiarb.tmdclient.analytics.AnalyticEvent

/**
 * @author ilya-rb on 19.02.19.
 */
interface AnalyticsTracker {

  companion object {
    const val TAG = "AnalyticsTracker"
  }

  fun sendEvent(event: AnalyticEvent)

}