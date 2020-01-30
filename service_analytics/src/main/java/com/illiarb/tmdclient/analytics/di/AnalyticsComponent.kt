package com.illiarb.tmdclient.analytics.di

import com.illiarb.tmdblcient.core.app.App
import com.illiarb.tmdblcient.core.di.providers.AnalyticsProvider
import dagger.Component

/**
 * @author ilya-rb on 20.02.19.
 */
@Component(modules = [AnalyticsModule::class])
interface AnalyticsComponent : AnalyticsProvider {

    companion object {
        fun get(app: App): AnalyticsComponent =
            DaggerAnalyticsComponent.builder()
                .analyticsModule(AnalyticsModule(app))
                .build()
    }
}