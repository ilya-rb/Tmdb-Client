package com.illiarb.tmdbclient.navigation

import android.widget.ImageView
import com.tmdbclient.servicetmdb.domain.Genre

/**
 * @author ilya-rb on 18.11.18.
 */
interface Router {

  fun executeAction(action: Action): Action

  sealed class Action {

    class ShowMovieDetails(val id: Int, val sharedPoster: ImageView? = null) : Action() {
      companion object {
        const val EXTRA_MOVIE_DETAILS = "id"
      }
    }

    class ShowDiscover(val id: Int = Genre.GENRE_ALL) : Action() {
      companion object {
        const val EXTRA_GENRE_ID = "id"
      }
    }

    class ShowVideos(val movieId: Int) : Action() {
      companion object {
        const val EXTRA_MOVIE_ID = "id"
      }
    }

    object ShowSettings : Action()
  }
}