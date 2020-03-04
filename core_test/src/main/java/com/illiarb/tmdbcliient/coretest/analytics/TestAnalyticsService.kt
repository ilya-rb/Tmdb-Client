package com.illiarb.tmdbcliient.coretest.analytics

import com.illiarb.tmdblcient.core.analytics.AnalyticEvent
import com.illiarb.tmdblcient.core.analytics.AnalyticsService
import com.illiarb.tmdblcient.core.navigation.Router

/**
 * @author ilya-rb on 22.02.19.
 */
class TestAnalyticsService : AnalyticsService {

  override fun trackEvent(event: AnalyticEvent) = Unit

  override fun trackRouterAction(action: Router.Action) = Unit
}