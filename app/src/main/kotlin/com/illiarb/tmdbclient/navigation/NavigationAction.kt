package com.illiarb.tmdbclient.navigation

import androidx.annotation.IdRes
import com.illiarb.tmdbclient.R

sealed class NavigationAction(
  @IdRes var destinationId: Int,
  val addOnTop: Boolean = false
) {

  companion object {
    const val NO_ID = 0
    const val EXTRA_MOVIE_DETAILS_MOVIE_ID = "movie_id"
    const val EXTRA_VIDEOS_MOVIE_ID = "movie_id"
    const val EXTRA_ADD_ON_TOP = "add_on_top"
  }

  sealed class Home(@IdRes destinationId: Int) : NavigationAction(destinationId) {

    object GoToDiscover : Home(R.id.action_home_to_discover)

    object GoToUiComponents : Home(R.id.action_home_to_ui_components)
  }

  data class MovieDetails(val id: Int) : NavigationAction(R.id.action_to_movie_details)

  sealed class Discover(@IdRes destinationId: Int) : NavigationAction(destinationId) {

    object GoToFilters : Discover(R.id.action_discover_to_filters)
  }

  sealed class WebViewAction(val url: String) : NavigationAction(NO_ID) {

    object GoToTmdbPage : WebViewAction("https://www.themoviedb.org")
  }

  sealed class DeepLink(@IdRes destinationId: Int) : NavigationAction(destinationId) {

    object Discover : DeepLink(R.id.deep_link_discover)
  }

  data class VideoList(val movieId: Int) : NavigationAction(NO_ID)

  object CloseVideoList : NavigationAction(NO_ID)

  object Exit : NavigationAction(NO_ID)
}