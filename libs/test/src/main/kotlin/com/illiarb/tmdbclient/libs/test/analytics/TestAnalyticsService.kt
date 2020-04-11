package com.illiarb.tmdbclient.libs.test.analytics

import com.illiarb.tmdbclient.services.analytics.AnalyticEvent
import com.illiarb.tmdbclient.services.analytics.AnalyticsService

/**
 * @author ilya-rb on 22.02.19.
 */
class TestAnalyticsService : AnalyticsService {

  override fun trackEvent(event: AnalyticEvent) = Unit

  override fun trackRouterAction(action: String) = Unit
}