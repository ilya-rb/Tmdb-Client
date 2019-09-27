package com.illiarb.tmdclient.analytics

import com.illiarb.tmdblcient.core.services.analytics.AnalyticEvent

/**
 * @author ilya-rb on 19.02.19.
 */
interface AnalyticsTracker {

    fun sendEvent(event: AnalyticEvent)

}