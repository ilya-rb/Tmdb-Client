package com.illiarb.tmdbclient.feature.movies.navigation

import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbclient.feature.movies.details.MovieDetailsFragment
import com.illiarb.tmdblcient.core.navigation.ShowMovieDetailsAction
import javax.inject.Inject

class ShowMovieDetailsActionImpl @Inject constructor(
    private val activity: FragmentActivity
) : ShowMovieDetailsAction {

    override fun showMovieDetails(id: Int) {
        val args = MovieDetailsFragment.createBundle(id)
        Navigation.findNavController(activity, R.id.nav_host_fragment).navigate(R.id.movieDetailsAction, args)
    }
}