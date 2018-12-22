package com.illiarb.tmdbclient.feature.movies.details.feature

import com.badoo.mvicore.element.WishToAction
import com.illiarb.tmdbclient.feature.movies.details.feature.MovieDetailsFeature.Action
import com.illiarb.tmdbclient.feature.movies.details.feature.MovieDetailsFeature.Wish
import javax.inject.Inject

/**
 * @author ilya-rb on 22.12.18.
 */
class MovieDetailsWishToAction @Inject constructor() : WishToAction<Wish, Action> {

    override fun invoke(wish: Wish): Action =
        when (wish) {
            is Wish.LoadMovieDetails -> Action.LoadMovieDetailsAction(wish.id)
        }
}