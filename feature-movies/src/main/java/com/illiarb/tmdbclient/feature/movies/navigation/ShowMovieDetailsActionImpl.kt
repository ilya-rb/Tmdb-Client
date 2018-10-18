package com.illiarb.tmdbclient.feature.movies.navigation

import androidx.fragment.app.FragmentActivity
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbclient.feature.movies.details.MovieDetailsFragment
import com.illiarb.tmdblcient.core.navigation.ShowMovieDetailsAction
import javax.inject.Inject

class ShowMovieDetailsActionImpl @Inject constructor(
    private val activity: FragmentActivity
) : ShowMovieDetailsAction {

    override fun showMovieDetails(id: Int) {
        activity.supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameContainer, MovieDetailsFragment.newInstance(id), MovieDetailsFragment::class.java.name)
            .addToBackStack(MovieDetailsFragment::class.java.name)
            .commit()
    }
}