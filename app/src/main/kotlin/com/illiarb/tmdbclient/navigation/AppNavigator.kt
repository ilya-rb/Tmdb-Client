package com.illiarb.tmdbclient.navigation

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.Navigator.Extras
import androidx.navigation.fragment.FragmentNavigator
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.navigation.Router.Action.ShowDiscover
import com.illiarb.tmdbclient.navigation.Router.Action.ShowMovieDetails
import com.illiarb.tmdbclient.navigation.Router.Action.ShowSettings
import com.illiarb.tmdbclient.navigation.Router.Action.ShowVideos
import javax.inject.Inject

/**
 * @author ilya-rb on 18.11.18.
 */
class AppNavigator @Inject constructor(private val activity: FragmentActivity) : Navigator {

  override fun executeAction(action: Router.Action) {
    val controller = Navigation.findNavController(activity, R.id.nav_host_fragment)
    val destination = when (action) {
      is ShowMovieDetails -> R.id.action_to_movie_details
      is ShowDiscover -> R.id.action_moviesFragment_to_discoverFragment
      is ShowSettings -> R.id.action_movies_to_settings
      is ShowVideos -> R.id.movie_to_player
    }
    controller.navigate(
      destination,
      setDestinationArgs(action),
      setNavOptions(),
      setNavExtras(action)
    )
  }

  private fun setDestinationArgs(action: Router.Action): Bundle {
    return when (action) {
      is ShowMovieDetails -> Bundle().apply {
        putInt(ShowMovieDetails.EXTRA_MOVIE_DETAILS, action.id)
      }
      is ShowDiscover -> Bundle().apply { putInt(ShowDiscover.EXTRA_GENRE_ID, action.id) }
      is ShowVideos -> Bundle().apply {
        putInt(ShowVideos.EXTRA_MOVIE_ID, action.movieId)
      }
      else -> Bundle.EMPTY
    }
  }

  private fun setNavOptions(): NavOptions = NavOptions.Builder().build()

  private fun setNavExtras(action: Router.Action): Extras? {
    return if (action is ShowMovieDetails && action.sharedPoster != null) {
      FragmentNavigator.Extras.Builder()
        .addSharedElement(action.sharedPoster, action.sharedPoster.transitionName)
        .build()
    } else {
      null
    }
  }
}