package com.illiarb.tmdclient.analytics.internal.trackers

import android.annotation.SuppressLint
import android.os.Bundle
import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.illiarb.tmdclient.analytics.AnalyticEvent
import com.illiarb.tmdclient.analytics.internal.AnalyticsTracker
import javax.inject.Inject

/**
 * @author ilya-rb on 19.02.19.
 */
internal class FirebaseAnalyticTracker @Inject constructor(app: Application) : AnalyticsTracker {

  @SuppressLint("MissingPermission")
  private val analytics = FirebaseAnalytics.getInstance(app)

  override fun sendEvent(event: AnalyticEvent) {
    analytics.logEvent(event.eventName, createBundleFromEvent(event))
  }

  private fun createBundleFromEvent(event: AnalyticEvent): Bundle =
    Bundle().apply {
      when (event) {
        is AnalyticEvent.RouterAction -> putString("screen_name", event.action)
      }
    }
}