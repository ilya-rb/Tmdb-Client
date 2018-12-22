package com.illiarb.tmdbclient.feature.movies.list.feature

import com.badoo.mvicore.feature.BaseFeature
import com.illiarb.tmdblcient.core.entity.MovieSection
import javax.inject.Inject

/**
 * @author ilya-rb on 22.12.18.
 */
class MoviesFeature @Inject constructor(
    moviesActor: MoviesActor,
    moviesWithToAction: MoviesWithToAction,
    reducer: MoviesReducer,
    newsPublisher: MoviesNewsPublisher
) : BaseFeature<MoviesFeature.Wish, MoviesFeature.Action, MoviesFeature.Effect, MoviesState, MoviesFeature.News>(
    initialState = MoviesState.idle(),
    wishToAction = moviesWithToAction,
    actor = moviesActor,
    reducer = reducer,
    newsPublisher = newsPublisher
) {

    sealed class Wish {
        object Refresh : Wish()
        data class ShowMovieDetails(val id: Int) : Wish()
    }

    sealed class Action {
        object Refresh : Action()
        data class ShowMovieDetails(val id: Int) : Action()
    }

    sealed class Effect {

        sealed class MoviesRefresh : Effect() {
            object InFlight : MoviesRefresh()
            data class Success(val movies: List<MovieSection>) : MoviesRefresh()
            data class Failure(val error: Throwable) : MoviesRefresh()
        }

        sealed class ShowMovieDetails : Effect() {
            data class Success(val id: Int) : ShowMovieDetails()
        }
    }

    sealed class News {
        data class ShowMovieDetails(val id: Int) : News()
    }
}