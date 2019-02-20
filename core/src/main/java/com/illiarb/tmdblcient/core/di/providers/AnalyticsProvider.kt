package com.illiarb.tmdblcient.core.di.providers

import com.illiarb.tmdblcient.core.analytics.AnalyticsService

/**
 * @author ilya-rb on 20.02.19.
 */
interface AnalyticsProvider {

    fun provideAnalyticsService(): AnalyticsService
}