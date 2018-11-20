package com.illiarb.tmdblcient.core.navigation

/**
 * @author ilya-rb on 18.11.18.
 */
sealed class NavigationData(val screenName: ScreenName)

object MoviesScreenData : NavigationData(ScreenName.MOVIES)
object ExploreScreenData : NavigationData(ScreenName.EXPLORE)
object AccountScreenData : NavigationData(ScreenName.ACCOUNT)
object AuthScreenData : NavigationData(ScreenName.AUTH)

class MovieDetailsData(
    val id: Int,
    val title: String,
    val posterPath: String?
) : NavigationData(ScreenName.MOVIE_DETAILS)