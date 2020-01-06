package com.illiarb.tmdbcliient.coretest.analytics

import com.illiarb.tmdblcient.core.services.analytics.AnalyticEvent
import com.illiarb.tmdblcient.core.services.analytics.AnalyticsEventFactory
import com.illiarb.tmdblcient.core.services.analytics.AnalyticsService

/**
 * @author ilya-rb on 22.02.19.
 */
class TestAnalyticsService : AnalyticsService {

    override val factory: AnalyticsEventFactory
        get() = TestAnalyticEventFactory()

    override fun trackEvent(event: AnalyticEvent) = Unit
}