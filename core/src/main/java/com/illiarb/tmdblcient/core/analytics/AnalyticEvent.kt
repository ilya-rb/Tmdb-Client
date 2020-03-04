package com.illiarb.tmdblcient.core.analytics

import com.illiarb.tmdblcient.core.navigation.Router

/**
 * @author ilya-rb on 19.02.19.
 */
sealed class AnalyticEvent(val eventName: String) {

  class RouterAction(val action: Router.Action) : AnalyticEvent("screen_opened")
}