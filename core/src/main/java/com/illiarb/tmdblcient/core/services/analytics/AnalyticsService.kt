package com.illiarb.tmdblcient.core.services.analytics

/**
 * @author ilya-rb on 20.02.19.
 */
interface AnalyticsService {

    val factory: AnalyticsEventFactory

    fun trackEvent(event: AnalyticEvent)
}