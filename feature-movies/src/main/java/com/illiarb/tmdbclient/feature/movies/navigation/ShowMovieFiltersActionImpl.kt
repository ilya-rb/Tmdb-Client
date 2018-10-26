package com.illiarb.tmdbclient.feature.movies.navigation

import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbclient.feature.movies.filters.MovieFiltersFragment
import com.illiarb.tmdblcient.core.navigation.ShowMovieFiltersAction
import javax.inject.Inject

/**
 * @author ilya-rb on 26.10.18.
 */
class ShowMovieFiltersActionImpl @Inject constructor(
    private val activity: FragmentActivity
) : ShowMovieFiltersAction {

    override fun showMovieFilters() {
        MovieFiltersFragment().show(activity.supportFragmentManager, MovieFiltersFragment::class.java.name)
    }
}