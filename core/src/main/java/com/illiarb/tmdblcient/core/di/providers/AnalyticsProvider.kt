package com.illiarb.tmdblcient.core.di.providers

import com.illiarb.tmdblcient.core.analytics.AnalyticsService

interface AnalyticsProvider {

    fun provideAnalyticsService(): AnalyticsService
}