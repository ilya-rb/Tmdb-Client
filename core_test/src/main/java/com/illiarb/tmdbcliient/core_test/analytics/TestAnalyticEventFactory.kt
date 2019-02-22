package com.illiarb.tmdbcliient.core_test.analytics

import com.illiarb.tmdblcient.core.analytics.AnalyticEvent
import com.illiarb.tmdblcient.core.analytics.AnalyticEventFactory
import com.illiarb.tmdblcient.core.navigation.ScreenName

/**
 * @author ilya-rb on 22.02.19.
 */
class TestAnalyticEventFactory : AnalyticEventFactory {

    override fun createMovieSearchEvent(query: String): AnalyticEvent =
        AnalyticEvent.MovieSearched(query)

    override fun createScreenOpenedEvent(screenName: ScreenName): AnalyticEvent =
        AnalyticEvent.ScreenOpened(screenName)

    override fun createLoggedInEvent(): AnalyticEvent = AnalyticEvent.LoggedIn

    override fun createLoggedOutEvent(): AnalyticEvent = AnalyticEvent.LoggedOut
}