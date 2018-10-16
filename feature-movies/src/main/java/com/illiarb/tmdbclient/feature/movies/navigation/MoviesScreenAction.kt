package com.illiarb.tmdbclient.feature.movies.navigation

import androidx.fragment.app.FragmentActivity
import com.illiarb.tmdbclient.feature.movies.movieslist.MoviesFragment
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdblcient.core.navigation.ShowMoviesListAction
import javax.inject.Inject

class MoviesScreenAction @Inject constructor(
    private val activity: FragmentActivity
) : ShowMoviesListAction {

    override fun showStartScreen() {
        activity.supportFragmentManager
            .beginTransaction()
            .add(R.id.frameContainer, MoviesFragment(), MoviesFragment::class.java.name)
            .commit()

//        Navigation
//            .findNavController(activity, R.id.nav_host_fragment)
//            .navigate(R.id.moviesFragmentAction)
    }
}