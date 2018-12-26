package com.illiarb.tmdbclient.features.main.navigation

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import com.illiarb.tmdbclient.features.main.R
import com.illiarb.tmdblcient.core.navigation.MovieDetailsScreen
import com.illiarb.tmdblcient.core.navigation.NavigationKeys
import com.illiarb.tmdblcient.core.navigation.Navigator
import com.illiarb.tmdblcient.core.navigation.ScreenData
import com.illiarb.tmdblcient.core.navigation.ScreenName
import javax.inject.Inject

/**
 * @author ilya-rb on 18.11.18.
 */
class MainNavigator @Inject constructor(
    private val activity: FragmentActivity
) : Navigator {

    override fun runNavigate(data: ScreenData) {
        val directions = createNavDirections(data)
        val controller = Navigation.findNavController(activity, R.id.nav_host_fragment)

        // If it's dynamic feature check if there is
        // destination for it
        if (isDynamicFeatureDirections(directions)) {
            val directionNode = controller.graph.findNode(directions.actionId)

            // First time dynamic feature launch
            // Add new node
            if (directionNode == null) {
                val id = directions.actionId
                val fragmentClass = when (data.screenName) {
                    ScreenName.AUTH -> DynamicFeature.Auth.className
                    ScreenName.ACCOUNT -> DynamicFeature.Account.className
                    else -> throw IllegalStateException("Unknown dynamic feature screen")
                }

                val destination = controller.navigatorProvider
                    .getNavigator(FragmentNavigator::class.java)
                    .createDestination()
                    .apply {
                        setId(id)
                        setFragmentClass(fragmentClass)
                    }

                controller.graph.addDestination(destination)
            }
        }

        controller.navigate(directions)
    }

    private fun createNavDirections(data: ScreenData): NavDirections =
        object : NavDirections {
            override fun getActionId(): Int = mapScreenNameToAction(data.screenName)
            override fun getArguments(): Bundle? = createNavigationArguments(data)
        }

    private fun createNavigationArguments(data: ScreenData): Bundle =
        when (data) {
            is MovieDetailsScreen -> Bundle().apply {
                putInt(NavigationKeys.EXTRA_MOVIE_DETAILS_ID, data.id)
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
        }

    private fun isDynamicFeatureDirections(directions: NavDirections): Boolean =
        directions.actionId == R.id.accountAction || directions.actionId == R.id.authAction
}