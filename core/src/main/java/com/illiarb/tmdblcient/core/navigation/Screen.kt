package com.illiarb.tmdblcient.core.navigation

/**
 * @author ilya-rb on 18.11.18.
 */
sealed class Screen(val screenName: ScreenName)

object AccountScreen : Screen(ScreenName.ACCOUNT)
object AuthScreen : Screen(ScreenName.AUTH)

data class MovieDetailsScreen(val id: Int) : Screen(ScreenName.MOVIE_DETAILS) {
    companion object {
        const val EXTRA_ID = "id"
    }
}