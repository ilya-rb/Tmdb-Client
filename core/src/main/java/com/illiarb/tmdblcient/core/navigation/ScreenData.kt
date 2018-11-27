package com.illiarb.tmdblcient.core.navigation

/**
 * @author ilya-rb on 18.11.18.
 */
sealed class ScreenData(val screenName: ScreenName)

object MoviesScreen : ScreenData(ScreenName.MOVIES)
object ExploreScreen : ScreenData(ScreenName.EXPLORE)
object AccountScreen : ScreenData(ScreenName.ACCOUNT)
object AuthScreen : ScreenData(ScreenName.AUTH)

class MovieDetailsScreen(
    val id: Int,
    val title: String,
    val posterPath: String?
) : ScreenData(ScreenName.MOVIE_DETAILS)