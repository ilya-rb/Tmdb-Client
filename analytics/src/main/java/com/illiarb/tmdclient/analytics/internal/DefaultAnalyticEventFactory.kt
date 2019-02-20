package com.illiarb.tmdclient.analytics.internal

import com.illiarb.tmdblcient.core.analytics.AnalyticEvent
import com.illiarb.tmdblcient.core.analytics.AnalyticEventFactory
import com.illiarb.tmdblcient.core.navigation.ScreenName
import javax.inject.Inject

/**
 * @author ilya-rb on 20.02.19.
 */
class DefaultAnalyticEventFactory @Inject constructor() : AnalyticEventFactory {

    override fun createMovieSearchEvent(query: String): AnalyticEvent =
        AnalyticEvent.MovieSearched(query)

    override fun createLoggedInEvent(): AnalyticEvent =
        AnalyticEvent.LoggedIn

    override fun createLoggedOutEvent(): AnalyticEvent =
        AnalyticEvent.LoggedOut

    override fun createScreenOpenedEvent(screenName: ScreenName): AnalyticEvent =
        AnalyticEvent.ScreenOpened(screenName)
}