package com.illiarb.tmdbclient.feature.movies.list.feature

import com.badoo.mvicore.element.NewsPublisher
import com.illiarb.tmdbclient.feature.movies.list.feature.MoviesFeature.*
import javax.inject.Inject

/**
 * @author ilya-rb on 22.12.18.
 */
class MoviesNewsPublisher @Inject constructor() : NewsPublisher<Action, Effect, MoviesState, News> {

    override fun invoke(action: Action, effect: Effect, state: MoviesState): News? =
        when (effect) {
            is Effect.ShowMovieDetails.Success -> News.ShowMovieDetails(effect.id)
            else -> null
        }
}