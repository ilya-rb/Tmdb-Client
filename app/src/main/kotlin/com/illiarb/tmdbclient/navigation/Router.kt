package com.illiarb.tmdbclient.navigation

import android.widget.ImageView
import androidx.annotation.IdRes
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.services.tmdb.domain.Genre
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author ilya-rb on 18.11.18.
 */
interface Router {

  fun executeAction(action: Action): Action

  sealed class Action(@IdRes val destinationId: Int = NO_ID) {

    companion object {
      const val NO_ID = 0
    }

    class ShowMovieDetails(val id: Int, val sharedPoster: ImageView? = null) :
      Action(R.id.action_to_movie_details) {

      companion object {
        const val EXTRA_MOVIE_DETAILS = "id"
      }
    }

    class ShowDiscover(val id: Int = Genre.GENRE_ALL) :
      Action(R.id.action_moviesFragment_to_discoverFragment) {

      companion object {
        const val EXTRA_GENRE_ID = "id"
      }
    }

    class ShowVideos(val movieId: Int) : Action(R.id.movie_to_player) {
      companion object {
        const val EXTRA_MOVIE_ID = "id"
      }
    }

    sealed class WebViewAction(val url: String) : Action() {
      object ShowTmdbPage : WebViewAction("https://www.themoviedb.org")
    }
  }

  @Singleton
  class DefaultRouter @Inject constructor(private val navigatorHolder: NavigatorHolder) : Router {

    override fun executeAction(action: Action): Action {
      navigatorHolder.executeAction(action)
      return action
    }
  }
}