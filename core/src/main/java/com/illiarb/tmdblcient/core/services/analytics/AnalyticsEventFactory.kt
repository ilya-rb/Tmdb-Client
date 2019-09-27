package com.illiarb.tmdblcient.core.services.analytics

import com.illiarb.tmdblcient.core.navigation.Router

/**
 * @author ilya-rb on 20.02.19.
 */
interface AnalyticEventFactory {

    fun createMovieSearchEvent(query: String): AnalyticEvent

    fun createLoggedInEvent(): AnalyticEvent

    fun createLoggedOutEvent(): AnalyticEvent

    fun createRouterActionEvent(action: Router.Action): AnalyticEvent
}