package com.illiarb.tmdbclient.navigation

import androidx.annotation.IdRes
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.services.tmdb.domain.Genre

sealed class NavigationAction(@IdRes val destinationId: Int) {

  companion object {
    const val NO_ID = 0

    const val EXTRA_MOVIE_DETAILS_MOVIE_ID = "movie_id"
    const val EXTRA_DISCOVER_GENRE_ID = "genre_id"
    const val EXTRA_VIDEOS_MOVIE_ID = "movie_id"
  }

  sealed class Home(@IdRes destinationId: Int) : NavigationAction(destinationId) {

    class GoToMovieDetails(val id: Int) : Home(R.id.action_home_to_movie_details)

    class GoToDiscover(val id: Int = Genre.GENRE_ALL) : Home(R.id.action_home_to_discover)

    object GoToUiComponents : Home(R.id.action_home_to_ui_components)
  }

  sealed class MovieDetails(@IdRes destinationId: Int) : NavigationAction(destinationId) {

    class GoToVideos(val id: Int) : MovieDetails(R.id.action_details_to_videos)

    class GoToMovieDetails(val id: Int) : MovieDetails(R.id.action_details_to_details)
  }

  sealed class Discover(@IdRes destinationId: Int) : NavigationAction(destinationId) {

    object GoToFilters : Discover(R.id.action_discover_to_filters)

    class GoToMovieDetails(val id: Int) : Discover(R.id.action_discover_to_movie_details)
  }

  sealed class Filters(@IdRes destinationId: Int) : NavigationAction(destinationId) {

    object BackToDiscover : Filters(R.id.action_filters_to_discover)
  }

  sealed class WebViewAction(val url: String) : NavigationAction(NO_ID) {

    object GoToTmdbPage : WebViewAction("https://www.themoviedb.org")
  }
}