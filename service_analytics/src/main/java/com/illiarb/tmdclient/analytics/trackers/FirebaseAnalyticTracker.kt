package com.illiarb.tmdclient.analytics.trackers

import android.annotation.SuppressLint
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.illiarb.tmdblcient.core.analytics.AnalyticEvent
import com.illiarb.tmdblcient.core.analytics.AnalyticEvent.RouterAction
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdclient.analytics.AnalyticsTracker
import javax.inject.Inject

/**
 * @author ilya-rb on 19.02.19.
 */
class FirebaseAnalyticTracker @Inject constructor(app: App) : AnalyticsTracker {

    @SuppressLint("MissingPermission")
    private val analytics = FirebaseAnalytics.getInstance(app.getApplication())

    override fun sendEvent(event: AnalyticEvent) {
        analytics.logEvent(event.eventName, createBundleFromEvent(event))
    }

    private fun createBundleFromEvent(event: AnalyticEvent): Bundle =
        Bundle().apply {
            when (event) {
                is RouterAction -> putString("screen_name", event.action.toString())
                else -> Bundle.EMPTY
            }
        }
}