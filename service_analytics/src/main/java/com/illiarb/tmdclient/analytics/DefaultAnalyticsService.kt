package com.illiarb.tmdclient.analytics

import com.illiarb.tmdblcient.core.analytics.AnalyticEvent
import com.illiarb.tmdblcient.core.analytics.AnalyticsService

/**
 * @author ilya-rb on 20.02.19.
 */
class DefaultAnalyticsService(private val eventTrackers: Set<AnalyticsTracker>) : AnalyticsService {

    override fun trackEvent(event: AnalyticEvent) {
        eventTrackers.forEach {
            it.sendEvent(event)
        }
    }
}