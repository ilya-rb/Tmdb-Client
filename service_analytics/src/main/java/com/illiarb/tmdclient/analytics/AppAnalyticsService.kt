package com.illiarb.tmdclient.analytics

import com.illiarb.tmdblcient.core.services.analytics.AnalyticEvent
import com.illiarb.tmdblcient.core.services.analytics.AnalyticsEventFactory
import com.illiarb.tmdblcient.core.services.analytics.AnalyticsService

/**
 * @author ilya-rb on 20.02.19.
 */
class AppAnalyticsService(
    private val eventFactory: AnalyticsEventFactory,
    private val eventTrackers: Set<AnalyticsTracker>
) : AnalyticsService {

    override val factory: AnalyticsEventFactory
        get() = eventFactory

    override fun trackEvent(event: AnalyticEvent) {
        eventTrackers.forEach {
            it.sendEvent(event)
        }
    }
}