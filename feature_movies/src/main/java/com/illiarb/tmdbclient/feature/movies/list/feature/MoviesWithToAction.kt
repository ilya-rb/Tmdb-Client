package com.illiarb.tmdbclient.feature.movies.list.feature

import com.badoo.mvicore.element.WishToAction
import com.illiarb.tmdbclient.feature.movies.list.feature.MoviesFeature.Action
import com.illiarb.tmdbclient.feature.movies.list.feature.MoviesFeature.Wish
import javax.inject.Inject

/**
 * @author ilya-rb on 22.12.18.
 */
class MoviesWithToAction @Inject constructor() : WishToAction<Wish, Action> {

    override fun invoke(wish: Wish): Action =
        when (wish) {
            is Wish.Refresh -> Action.Refresh
            is Wish.ShowMovieDetails -> Action.ShowMovieDetails(wish.id)
        }
}