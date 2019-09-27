package com.illiarb.tmdblcient.core.services.analytics

import com.illiarb.tmdblcient.core.navigation.Router

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

    data class RouterAction(val action: Router.Action) : AnalyticEvent("screen_opened")

    data class MovieSearched(val query: String) : AnalyticEvent("movie_searched")

    object LoggedIn : AnalyticEvent("logged_in")

    object LoggedOut : AnalyticEvent("logged_out")
}