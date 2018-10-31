package com.illiarb.tmdblcient.core.navigation

/**
 * Interface that contain all main
 * application routes
 *
 * @author ilya-rb on 30.10.18.
 */
interface Navigator {

    fun showMoviesScreen()

    fun showMovieDetailsScreen(movieId: Int)

    fun showExploreScreen()
}