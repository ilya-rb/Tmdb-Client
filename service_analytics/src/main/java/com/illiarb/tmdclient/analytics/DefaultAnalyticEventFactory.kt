package com.illiarb.tmdclient.analytics

import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.services.analytics.AnalyticEvent
import com.illiarb.tmdblcient.core.services.analytics.AnalyticsEventFactory
import javax.inject.Inject

/**
 * @author ilya-rb on 20.02.19.
 */
class DefaultAnalyticEventFactory @Inject constructor() : AnalyticsEventFactory {

    override fun createMovieSearchEvent(query: String): AnalyticEvent =
        AnalyticEvent.MovieSearched(query)

    override fun createLoggedInEvent(): AnalyticEvent =
        AnalyticEvent.LoggedIn

    override fun createLoggedOutEvent(): AnalyticEvent =
        AnalyticEvent.LoggedOut

    override fun createRouterActionEvent(action: Router.Action): AnalyticEvent =
        AnalyticEvent.RouterAction(action)
}