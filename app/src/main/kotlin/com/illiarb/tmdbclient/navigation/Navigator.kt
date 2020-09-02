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
import com.illiarb.tmdbclient.libs.tools.FeatureFlagStore
import com.illiarb.tmdbclient.libs.tools.FeatureFlagStore.FeatureFlag
import com.illiarb.tmdbclient.navigation.NavigationAction.Companion.EXTRA_ADD_ON_TOP
import com.illiarb.tmdbclient.navigation.NavigationAction.Companion.EXTRA_MOVIE_DETAILS_MOVIE_ID
import com.illiarb.tmdbclient.navigation.NavigationAction.MovieDetails
import com.illiarb.tmdbclient.navigation.video.MovieVideosNavigator
import javax.inject.Inject

/**
 * @author ilya-rb on 18.11.18.
 */
interface Navigator {

  fun executeAction(action: NavigationAction)

  class DefaultNavigator @Inject constructor(
    private val activity: FragmentActivity,
    private val featureFlagStore: FeatureFlagStore,
    private val movieVideosNavigator: MovieVideosNavigator,
  ) : Navigator {

    override fun executeAction(action: NavigationAction) {
      val controller = Navigation.findNavController(activity, R.id.nav_host_fragment)

      if (action is MovieDetails
        && featureFlagStore.isFeatureEnabled(FeatureFlag.MOVIE_DETAILS_COMPOSE)
      ) {
        action.destinationId = R.id.action_to_movie_details_compose
      }

      when (action) {
        is NavigationAction.WebViewAction -> showWebView(action)
        is NavigationAction.Exit -> controller.popBackStack()
        is NavigationAction.VideoList -> movieVideosNavigator.executeVideoListAction(action)
        is NavigationAction.CloseVideoList -> movieVideosNavigator.executeCloseVideoListAction()
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
        putBoolean(EXTRA_ADD_ON_TOP, action.addOnTop)

        when (action) {
          is MovieDetails -> putInt(EXTRA_MOVIE_DETAILS_MOVIE_ID, action.id)
          else -> {
          }
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
          .build(),
        Uri.parse(action.url),
        WebViewFallback()
      )
    }
  }
}