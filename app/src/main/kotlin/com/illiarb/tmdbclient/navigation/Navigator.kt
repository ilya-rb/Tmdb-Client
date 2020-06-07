package com.illiarb.tmdbclient.navigation

import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.libs.customtabs.CustomTabsHelper
import com.illiarb.tmdbclient.libs.customtabs.WebViewFallback
import com.illiarb.tmdbclient.navigation.NavigationAction.Companion.EXTRA_MOVIE_DETAILS_MOVIE_ID
import com.illiarb.tmdbclient.navigation.NavigationAction.Companion.EXTRA_VIDEOS_MOVIE_ID
import com.illiarb.tmdbclient.navigation.NavigationAction.Discover
import com.illiarb.tmdbclient.navigation.NavigationAction.Home
import com.illiarb.tmdbclient.navigation.NavigationAction.MovieDetails
import javax.inject.Inject

/**
 * @author ilya-rb on 18.11.18.
 */
interface Navigator {

  fun executeAction(action: NavigationAction)

  class DefaultNavigator @Inject constructor(private val activity: FragmentActivity) : Navigator {

    override fun executeAction(action: NavigationAction) {
      val controller = Navigation.findNavController(activity, R.id.nav_host_fragment)

      when (action) {
        is NavigationAction.WebViewAction -> showWebView(action)
        is NavigationAction.Exit -> controller.popBackStack()
        else -> controller.navigate(
          action.destinationId,
          setDestinationArgs(action),
          setNavOptions(),
          setNavExtras(action)
        )
      }
    }

    private fun setDestinationArgs(action: NavigationAction): Bundle {
      return Bundle().apply {
        when (action) {
          is Home.GoToMovieDetails -> putInt(EXTRA_MOVIE_DETAILS_MOVIE_ID, action.id)
          is Discover.GoToMovieDetails -> putInt(EXTRA_MOVIE_DETAILS_MOVIE_ID, action.id)
          is MovieDetails.GoToVideos -> putInt(EXTRA_VIDEOS_MOVIE_ID, action.id)
        }
      }
    }

    private fun setNavOptions(): NavOptions = NavOptions.Builder().build()

    private fun setNavExtras(action: NavigationAction): androidx.navigation.Navigator.Extras? = null

    private fun showWebView(action: NavigationAction.WebViewAction) {
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