package com.illiarb.tmdbclient.navigation

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.illiarb.tmdbclient.R
import com.illiarb.tmdblcient.core.navigation.Navigator
import com.illiarb.tmdblcient.core.navigation.Router
import javax.inject.Inject

/**
 * @author ilya-rb on 18.11.18.
 */
class AppNavigator @Inject constructor(private val activity: FragmentActivity) : Navigator {

    override fun executeAction(action: Router.Action) {
        val controller = Navigation.findNavController(activity, R.id.nav_host_fragment)
        val destination = when (action) {
            is Router.Action.ShowMovieDetails -> R.id.action_moviesFragment_to_movieDetailsFragment
            is Router.Action.ShowDiscover -> R.id.action_moviesFragment_to_discoverFragment
        }
        controller.navigate(destination, setDestinationArgs(action), setNavOptions())
    }

    private fun setDestinationArgs(action: Router.Action): Bundle {
        return when (action) {
            is Router.Action.ShowMovieDetails -> Bundle().apply {
                putInt(Router.Action.ShowMovieDetails.EXTRA_MOVIE_DETAILS, action.id)
            }
            else -> Bundle.EMPTY
        }
    }

    private fun setNavOptions(): NavOptions =
        NavOptions.Builder()
            .setEnterAnim(R.anim.anim_fade_in)
            .setExitAnim(R.anim.anim_fade_out)
            .build()
}