package com.illiarb.tmdclient.analytics.trackers

import com.illiarb.tmdblcient.core.analytics.AnalyticEvent
import com.illiarb.tmdblcient.core.system.Logger
import com.illiarb.tmdclient.analytics.AnalyticsTracker
import javax.inject.Inject

/**
 * @author ilya-rb on 20.02.19.
 */
class DebugAnalyticsTracker @Inject constructor() : AnalyticsTracker {

    override fun sendEvent(event: AnalyticEvent) {
        Logger.i("Analytics event occurred: $event")
    }
}