package com.illiarb.tmdbclient.services.analytics.internal

import com.illiarb.tmdbclient.services.analytics.AnalyticEvent
import com.illiarb.tmdbclient.services.analytics.AnalyticsService

/**
 * @author ilya-rb on 20.02.19.
 */
internal class DefaultAnalyticsService(
  private val eventTrackers: Set<AnalyticsTracker>
) : AnalyticsService {

  override fun trackEvent(event: AnalyticEvent) {
    eventTrackers.forEach {
      it.sendEvent(event)
    }
  }
}