package com.illiarb.tmdbclient.navigation

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import com.illiarb.tmdbclient.R
import javax.inject.Inject

/**
 * @author ilya-rb on 18.11.18.
 */
interface Navigator {

  fun executeAction(action: Router.Action)

  class DefaultNavigator @Inject constructor(private val activity: FragmentActivity) : Navigator {

    override fun executeAction(action: Router.Action) {
      val controller = Navigation.findNavController(activity, R.id.nav_host_fragment)
      val destination = when (action) {
        is Router.Action.ShowMovieDetails -> R.id.action_to_movie_details
        is Router.Action.ShowDiscover -> R.id.action_moviesFragment_to_discoverFragment
        is Router.Action.ShowVideos -> R.id.movie_to_player
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
        is Router.Action.ShowMovieDetails -> Bundle().apply {
          putInt(Router.Action.ShowMovieDetails.EXTRA_MOVIE_DETAILS, action.id)
        }
        is Router.Action.ShowDiscover -> Bundle().apply {
          putInt(Router.Action.ShowDiscover.EXTRA_GENRE_ID, action.id)
        }
        is Router.Action.ShowVideos -> Bundle().apply {
          putInt(Router.Action.ShowVideos.EXTRA_MOVIE_ID, action.movieId)
        }
      }
    }

    private fun setNavOptions(): NavOptions = NavOptions.Builder().build()

    private fun setNavExtras(action: Router.Action): androidx.navigation.Navigator.Extras? {
      return if (action is Router.Action.ShowMovieDetails && action.sharedPoster != null) {
        FragmentNavigator.Extras.Builder()
          .addSharedElement(action.sharedPoster, action.sharedPoster.transitionName)
          .build()
      } else {
        null
      }
    }
  }
}