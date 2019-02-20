package com.illiarb.tmdblcient.core.analytics

import com.illiarb.tmdblcient.core.navigation.ScreenName

/**
 * * Analytic events:
 * - Open screen(screen_name string)
 * - Search movie(movie movie)
 * - Log in
 * - Log out
 *
 * @author ilya-rb on 19.02.19.
 */
sealed class AnalyticEvent(val eventName: String) {

    data class ScreenOpened(val screenName: ScreenName) : AnalyticEvent("screen_opened")

    data class MovieSearched(val query: String) : AnalyticEvent("movie_searched")

    object LoggedIn : AnalyticEvent("logged_in")

    object LoggedOut : AnalyticEvent("logged_out")
}