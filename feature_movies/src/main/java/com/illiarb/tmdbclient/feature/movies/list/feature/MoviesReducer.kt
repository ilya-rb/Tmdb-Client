package com.illiarb.tmdbclient.feature.movies.list.feature

import com.badoo.mvicore.element.Reducer
import com.illiarb.tmdbclient.feature.movies.list.feature.MoviesFeature.Effect
import javax.inject.Inject

/**
 * @author ilya-rb on 22.12.18.
 */
class MoviesReducer @Inject constructor() : Reducer<MoviesState, Effect> {

    override fun invoke(state: MoviesState, effect: Effect): MoviesState =
        when (effect) {
            is Effect.MoviesRefresh.InFlight -> state.copy(isLoading = true)
            is Effect.ShowMovieDetails.Success -> state

            is Effect.MoviesRefresh.Success ->
                state.copy(
                    isLoading = false,
                    movies = effect.movies,
                    error = null
                )

            is Effect.MoviesRefresh.Failure ->
                state.copy(
                    isLoading = false,
                    error = effect.error
                )
        }
}