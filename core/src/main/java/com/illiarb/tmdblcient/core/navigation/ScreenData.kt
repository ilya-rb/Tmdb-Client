package com.illiarb.tmdblcient.core.navigation

/**
 * @author ilya-rb on 18.11.18.
 */
sealed class ScreenData(val screenName: ScreenName)

object AccountScreen : ScreenData(ScreenName.ACCOUNT)
object AuthScreen : ScreenData(ScreenName.AUTH)
object SearchScreen : ScreenData(ScreenName.SEARCH)

data class MovieDetailsScreen(val id: Int) : ScreenData(ScreenName.MOVIE_DETAILS)