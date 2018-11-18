package com.illiarb.tmdblcient.core.navigation

/**
 * @author ilya-rb on 18.11.18.
 */
sealed class NavigationData(val screenName: ScreenName)

class MovieDetailsData(
    val id: Int,
    val title: String,
    val posterPath: String?
) : NavigationData(ScreenName.MOVIE_DETAILS)