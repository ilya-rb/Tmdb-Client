package com.illiarb.tmdbclient.features.main.navigation

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
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
class MainNavigator @Inject constructor(private val activity: FragmentActivity) : Navigator {

    override fun runNavigate(data: ScreenData) {
        val directions = createNavDirections(data)
        Navigation.findNavController(activity, R.id.nav_host_fragment).navigate(directions)
    }

    private fun createNavDirections(data: ScreenData): NavDirections {
        val args = createNavigationArguments(data)
        val action = when (data.screenName) {
            ScreenName.MOVIES -> R.id.moviesFragmentAction
            ScreenName.MOVIE_DETAILS -> R.id.movieDetailsAction
            ScreenName.EXPLORE -> R.id.exploreAction
            ScreenName.ACCOUNT -> R.id.accountAction
            ScreenName.AUTH -> R.id.authAction
            else -> TODO("Implement other screens")
        }

        return object : NavDirections {
            override fun getActionId(): Int = action
            override fun getArguments(): Bundle? = args
        }
    }

    private fun createNavigationArguments(data: ScreenData): Bundle =
        when (data) {
            is MovieDetailsScreen -> Bundle().apply {
                putInt(NavigationKeys.EXTRA_MOVIE_DETAILS_ID, data.id)
                putString(NavigationKeys.EXTRA_MOVIE_DETAILS_TITLE, data.title)
                putString(NavigationKeys.EXTRA_MOVIE_DETAIL_POSTER_PATH, data.posterPath)
            }
            else -> Bundle.EMPTY
        }
}