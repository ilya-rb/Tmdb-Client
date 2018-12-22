package com.illiarb.tmdbclient.feature.movies.details.feature

import com.badoo.mvicore.element.Reducer
import com.illiarb.tmdbclient.feature.movies.details.feature.MovieDetailsFeature.Effect
import javax.inject.Inject

/**
 * @author ilya-rb on 22.12.18.
 */
class MovieDetailsReducer @Inject constructor() : Reducer<MovieDetailsState, Effect> {

    override fun invoke(state: MovieDetailsState, effect: Effect): MovieDetailsState =
        when (effect) {
            is Effect.LoadMovieDetailsEffect -> {
                when (effect) {
                    is Effect.LoadMovieDetailsEffect.InFlight ->
                        state.copy(isLoading = true)

                    is Effect.LoadMovieDetailsEffect.Success ->
                        state.copy(isLoading = false, movie = effect.movie)

                    is Effect.LoadMovieDetailsEffect.Failure ->
                        state.copy(isLoading = false, error = effect.error)
                }
            }
        }
}