package com.illiarb.tmdblcient.core.analytics

/**
 * @author ilya-rb on 20.02.19.
 */
interface AnalyticsService {

    fun trackEvent(event: AnalyticEvent)
}