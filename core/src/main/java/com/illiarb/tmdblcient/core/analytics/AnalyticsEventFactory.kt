package com.illiarb.tmdblcient.core.analytics

import com.illiarb.tmdblcient.core.navigation.ScreenName

/**
 * @author ilya-rb on 20.02.19.
 */
interface AnalyticEventFactory {

    fun createMovieSearchEvent(query: String): AnalyticEvent

    fun createLoggedInEvent(): AnalyticEvent

    fun createLoggedOutEvent(): AnalyticEvent

    fun createScreenOpenedEvent(screenName: ScreenName): AnalyticEvent
}