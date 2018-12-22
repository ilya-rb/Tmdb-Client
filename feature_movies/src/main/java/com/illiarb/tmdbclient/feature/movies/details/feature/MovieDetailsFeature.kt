package com.illiarb.tmdbclient.feature.movies.details.feature

import com.badoo.mvicore.feature.BaseFeature
import com.illiarb.tmdbclient.feature.movies.details.feature.MovieDetailsFeature.*
import com.illiarb.tmdblcient.core.entity.Movie
import javax.inject.Inject

/**
 * @author ilya-rb on 22.12.18.
 */
class MovieDetailsFeature @Inject constructor(
    actor: MovieDetailsActor,
    reducer: MovieDetailsReducer,
    wishToAction: MovieDetailsWishToAction
) : BaseFeature<Wish, Action, Effect, MovieDetailsState, Nothing>(
    initialState = MovieDetailsState.idle(),
    wishToAction = wishToAction,
    reducer = reducer,
    actor = actor
) {

    sealed class Wish {
        data class LoadMovieDetails(val id: Int) : Wish()
    }

    sealed class Effect {

        sealed class LoadMovieDetailsEffect : Effect() {
            object InFlight : LoadMovieDetailsEffect()
            data class Success(val movie: Movie) : LoadMovieDetailsEffect()
            data class Failure(val error: Throwable) : LoadMovieDetailsEffect()
        }
    }

    sealed class Action {
        data class LoadMovieDetailsAction(val id: Int) : Action()
    }
}