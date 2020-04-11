package com.illiarb.tmdclient.analytics

/**
 * @author ilya-rb on 20.02.19.
 */
interface AnalyticsService {

  fun trackEvent(event: AnalyticEvent)

  fun trackRouterAction(action: String)
}