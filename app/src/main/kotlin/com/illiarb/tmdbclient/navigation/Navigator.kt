package com.illiarb.tmdbclient.navigation

import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.libs.customtabs.CustomTabsHelper
import com.illiarb.tmdbclient.libs.customtabs.WebViewFallback
import com.illiarb.tmdbclient.navigation.Router.Action
import javax.inject.Inject

/**
 * @author ilya-rb on 18.11.18.
 */
interface Navigator {

  fun executeAction(action: Action)

  class DefaultNavigator @Inject constructor(private val activity: FragmentActivity) : Navigator {

    override fun executeAction(action: Action) {
      val controller = Navigation.findNavController(activity, R.id.nav_host_fragment)

      if (action is Action.WebViewAction) {
        showWebView(action)
      } else {
        controller.navigate(
          action.destinationId,
          setDestinationArgs(action),
          setNavOptions(),
          setNavExtras(action)
        )
      }
    }

    private fun setDestinationArgs(action: Action): Bundle {
      return when (action) {
        is Action.ShowMovieDetails -> Bundle().apply {
          putInt(Action.ShowMovieDetails.EXTRA_MOVIE_DETAILS, action.id)
        }
        is Action.ShowDiscover -> Bundle().apply {
          putInt(Action.ShowDiscover.EXTRA_GENRE_ID, action.id)
        }
        is Action.ShowVideos -> Bundle().apply {
          putInt(Action.ShowVideos.EXTRA_MOVIE_ID, action.movieId)
        }
        else -> Bundle.EMPTY
      }
    }

    private fun setNavOptions(): NavOptions = NavOptions.Builder().build()

    private fun setNavExtras(action: Action): androidx.navigation.Navigator.Extras? {
      return if (action is Action.ShowMovieDetails && action.sharedPoster != null) {
        FragmentNavigator.Extras.Builder()
          .addSharedElement(action.sharedPoster, action.sharedPoster.transitionName)
          .build()
      } else {
        null
      }
    }

    private fun showWebView(action: Action.WebViewAction) {
      CustomTabsHelper.openCustomTab(
        activity,
        CustomTabsIntent.Builder()
          .setShowTitle(true)
          .enableUrlBarHiding()
          .build(),
        Uri.parse(action.url),
        WebViewFallback()
      )
    }
  }
}