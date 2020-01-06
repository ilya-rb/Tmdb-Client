package com.illiarb.tmdbcliient.coretest.analytics

import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.services.analytics.AnalyticEvent
import com.illiarb.tmdblcient.core.services.analytics.AnalyticsEventFactory

/**
 * @author ilya-rb on 22.02.19.
 */
class TestAnalyticEventFactory : AnalyticsEventFactory {

    override fun createMovieSearchEvent(query: String): AnalyticEvent =
        AnalyticEvent.MovieSearched(query)

    override fun createRouterActionEvent(action: Router.Action): AnalyticEvent =
        AnalyticEvent.RouterAction(action)

    override fun createLoggedInEvent(): AnalyticEvent = AnalyticEvent.LoggedIn

    override fun createLoggedOutEvent(): AnalyticEvent = AnalyticEvent.LoggedOut
}