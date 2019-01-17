package com.illiarb.tmdblcient.core.navigation

import android.view.View

/**
 * @author ilya-rb on 18.11.18.
 */
sealed class ScreenData(val screenName: ScreenName)

object AccountScreen : ScreenData(ScreenName.ACCOUNT)
object AuthScreen : ScreenData(ScreenName.AUTH)
object SearchScreen : ScreenData(ScreenName.SEARCH)

data class MovieDetailsScreen(val id: Int) : ScreenData(ScreenName.MOVIE_DETAILS)

data class PhotoViewScreen(
    // Shared element
    val photoView: View,
    val photos: List<String>,
    val selectedPhoto: String
) : ScreenData(ScreenName.PHOTO_VIEW) {

    companion object {
        const val SHARED_ELEMENT_NAME = "photoView"
    }
}