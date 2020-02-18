package com.illiarb.tmdclient.analytics

import com.illiarb.tmdblcient.core.analytics.AnalyticEvent
import com.illiarb.tmdblcient.core.analytics.AnalyticsService
import com.illiarb.tmdblcient.core.navigation.Router

/**
 * @author ilya-rb on 20.02.19.
 */
class DefaultAnalyticsService(private val eventTrackers: Set<AnalyticsTracker>) : AnalyticsService {

    override fun trackEvent(event: AnalyticEvent) {
        eventTrackers.forEach {
            it.sendEvent(event)
        }
    }

    override fun trackRouterAction(action: Router.Action) {
        eventTrackers.forEach {
            it.sendEvent(AnalyticEvent.RouterAction(action))
        }
    }
}