package com.illiarb.tmdbclient.feature.movies.navigation

import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdblcient.core.navigation.ShowMoviesListAction
import javax.inject.Inject

class ShowMoviesListActionImpl @Inject constructor(
    private val activity: FragmentActivity
) : ShowMoviesListAction {

    override fun showStartScreen() {
        Navigation.findNavController(activity, R.id.nav_host_fragment).navigate(R.id.moviesFragmentAction)
    }
}