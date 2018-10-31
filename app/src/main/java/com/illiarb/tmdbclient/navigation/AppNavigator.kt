package com.illiarb.tmdbclient.navigation

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavDirections
import com.illiarb.tmdbclient.R
import com.illiarb.tmdblcient.core.navigation.NavigationExtras
import com.illiarb.tmdblcient.core.navigation.Navigator
import com.illiarb.tmdblcient.core.system.EventBus
import java.io.Serializable
import javax.inject.Inject

/**
 * @author ilya-rb on 30.10.18.
 */
class AppNavigator @Inject constructor(private val bus: EventBus) : Navigator {

    override fun showMoviesScreen() {
        bus.postEvent(createNavigationData(R.id.moviesFragmentAction))
    }

    override fun showMovieDetailsScreen(movieId: Int) {
        bus.postEvent(
            createNavigationData(
                R.id.movieDetailsAction,
                createArguments(
                    NavigationExtras.EXTRA_MOVIE_DETAILS_ID to movieId
                )
            )
        )
    }

    private fun createNavigationData(actionId: Int, args: Bundle? = null): NavDirections =
        object : NavDirections {
            override fun getArguments(): Bundle? = args
            override fun getActionId(): Int = actionId
        }

    private fun createArguments(vararg args: Pair<String, Any>): Bundle =
        Bundle().apply {
            args.forEach { (key, value) ->
                when (value) {
                    is Int -> putInt(key, value)
                    is String -> putString(key, value)
                    is Boolean -> putBoolean(key, value)
                    is Long -> putLong(key, value)
                    is Float -> putFloat(key, value)
                    is Serializable -> putSerializable(key, value)
                    is Parcelable -> putParcelable(key, value)
                }
            }
        }
}