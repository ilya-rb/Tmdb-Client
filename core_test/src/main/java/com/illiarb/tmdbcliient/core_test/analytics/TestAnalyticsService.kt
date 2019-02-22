package com.illiarb.tmdbcliient.core_test.analytics

import com.illiarb.tmdblcient.core.analytics.AnalyticEvent
import com.illiarb.tmdblcient.core.analytics.AnalyticEventFactory
import com.illiarb.tmdblcient.core.analytics.AnalyticsService

/**
 * @author ilya-rb on 22.02.19.
 */
class TestAnalyticsService : AnalyticsService {

    override val factory: AnalyticEventFactory
        get() = TestAnalyticEventFactory()

    override fun trackEvent(event: AnalyticEvent) {
        // Do Nothing
    }
}