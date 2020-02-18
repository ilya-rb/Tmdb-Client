package com.illiarb.tmdblcient.core.analytics

import com.illiarb.tmdblcient.core.navigation.Router

/**
 * @author ilya-rb on 20.02.19.
 */
interface AnalyticsService {

    fun trackEvent(event: AnalyticEvent)

    fun trackRouterAction(action: Router.Action)
}