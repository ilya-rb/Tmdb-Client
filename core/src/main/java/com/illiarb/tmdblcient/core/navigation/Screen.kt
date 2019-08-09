package com.illiarb.tmdblcient.core.navigation

import android.view.View

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

data class PhotoViewScreen(
    // Shared element
    val photoView: View,
    val photos: List<String>,
    val selectedPhoto: String
) : Screen(ScreenName.PHOTO_VIEW) {

    companion object {
        const val EXTRA_PHOTOS = "photos"
        const val EXTRA_SELECTED = "selected"
    }
}