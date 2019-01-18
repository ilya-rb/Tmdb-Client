package com.illiarb.tmdbclient.main.navigation

import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import com.illiarb.tmdbclient.R
import com.illiarb.tmdblcient.core.navigation.*
import javax.inject.Inject

/**
 * @author ilya-rb on 18.11.18.
 */
class MainNavigator @Inject constructor(
    private val activity: FragmentActivity
) : Navigator {

    override fun runNavigate(data: ScreenData) {
        val action = mapScreenNameToAction(data.screenName)
        val controller = Navigation.findNavController(activity, R.id.nav_host_fragment)

        // TODO: Fix dynamic feature rotation screen

        // If it's dynamic feature check if there is
        // destination for it
        if (isDynamicFeatureAction(action)) {
            val directionNode = controller.graph.findNode(action)

            // First time dynamic feature launch
            // Add new node
            if (directionNode == null) {
                val fragmentClass = when (data.screenName) {
                    ScreenName.AUTH -> DynamicFeature.Auth.className
                    ScreenName.ACCOUNT -> DynamicFeature.Account.className
                    else -> throw IllegalStateException("Unknown dynamic feature screen")
                }

                val destination = controller.navigatorProvider
                    .getNavigator(FragmentNavigator::class.java)
                    .createDestination()
                    .apply {
                        id = action
                        className = fragmentClass
                    }

                controller.graph.addDestination(destination)
            }
        }

        controller.navigate(
            action,
            createNavigationArguments(data),
            createNavigationOptions(),
            createNavigationExtras(data)
        )
    }

    private fun createNavigationOptions(): NavOptions =
        NavOptions.Builder()
            .setEnterAnim(R.anim.anim_fade_in)
            .setExitAnim(R.anim.anim_fade_out)
            .build()

    private fun createNavigationExtras(data: ScreenData): FragmentNavigator.Extras? =
        when (data) {
            is PhotoViewScreen -> FragmentNavigator.Extras.Builder().build()
            else -> null
        }

    private fun createNavigationArguments(data: ScreenData): Bundle =
        when (data) {
            is MovieDetailsScreen -> Bundle().apply {
                putInt(NavigationKeys.EXTRA_MOVIE_DETAILS_ID, data.id)
            }

            is PhotoViewScreen -> Bundle().apply {
                putStringArrayList(NavigationKeys.EXTRA_PHOTOS, ArrayList(data.photos))
                putString(NavigationKeys.EXTRA_SELECTED_PHOTO, data.selectedPhoto)
            }

            else -> Bundle.EMPTY
        }

    private fun mapScreenNameToAction(screenName: ScreenName): Int =
        when (screenName) {
            ScreenName.MOVIES -> R.id.moviesFragmentAction
            ScreenName.MOVIE_DETAILS -> R.id.movieDetailsAction
            ScreenName.ACCOUNT -> R.id.accountAction
            ScreenName.AUTH -> R.id.authAction
            ScreenName.SEARCH -> R.id.searchAction
            ScreenName.PHOTO_VIEW -> R.id.photoViewAction
        }

    private fun isDynamicFeatureAction(action: Int): Boolean =
        action == R.id.accountAction || action == R.id.authAction
}