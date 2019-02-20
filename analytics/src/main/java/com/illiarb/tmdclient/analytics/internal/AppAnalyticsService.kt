package com.illiarb.tmdclient.analytics.internal

import com.illiarb.tmdblcient.core.analytics.AnalyticEvent
import com.illiarb.tmdblcient.core.analytics.AnalyticEventFactory
import com.illiarb.tmdblcient.core.analytics.AnalyticsService
import com.illiarb.tmdclient.analytics.AnalyticsTracker

/**
 * @author ilya-rb on 20.02.19.
 */
class AppAnalyticsService(
    private val eventFactory: AnalyticEventFactory,
    private val eventTrackers: Set<AnalyticsTracker>
) : AnalyticsService {

    override val factory: AnalyticEventFactory
        get() = eventFactory

    override fun trackEvent(event: AnalyticEvent) {
        eventTrackers.forEach {
            it.sendEvent(event)
        }
    }
}