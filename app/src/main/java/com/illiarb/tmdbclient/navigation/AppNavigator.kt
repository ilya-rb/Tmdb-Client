package com.illiarb.tmdbclient.navigation

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.illiarb.tmdbclient.R
import com.illiarb.tmdblcient.core.navigation.Navigator
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.navigation.Router.Action.PlayVideo
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowDiscover
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowMovieDetails
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowSettings
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
            is PlayVideo -> R.id.movie_to_player
        }
        controller.navigate(destination, setDestinationArgs(action), setNavOptions())
    }

    private fun setDestinationArgs(action: Router.Action): Bundle {
        return when (action) {
            is ShowMovieDetails -> Bundle().apply {
                putInt(ShowMovieDetails.EXTRA_MOVIE_DETAILS, action.id)
            }
            is ShowDiscover -> Bundle().apply {
                putInt(ShowDiscover.EXTRA_GENRE_ID, action.id)
            }
            is PlayVideo -> Bundle().apply {
                putString(PlayVideo.EXTRA_VIDEO_ID, action.videoId)
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