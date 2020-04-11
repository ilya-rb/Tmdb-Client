package com.illiarb.tmdbcliient.coretest.analytics

import com.illiarb.tmdclient.analytics.AnalyticEvent
import com.illiarb.tmdclient.analytics.AnalyticsService

/**
 * @author ilya-rb on 22.02.19.
 */
class TestAnalyticsService : AnalyticsService {

  override fun trackEvent(event: AnalyticEvent) = Unit

  override fun trackRouterAction(action: String) = Unit
}