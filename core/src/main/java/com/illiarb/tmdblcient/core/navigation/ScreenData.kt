package com.illiarb.tmdblcient.core.navigation

/**
 * @author ilya-rb on 18.11.18.
 */
sealed class ScreenData(val screenName: ScreenName)

object MoviesScreen : ScreenData(ScreenName.MOVIES)
object AccountScreen : ScreenData(ScreenName.ACCOUNT)
object AuthScreen : ScreenData(ScreenName.AUTH)

data class MovieDetailsScreen(val id: Int) : ScreenData(ScreenName.MOVIE_DETAILS)