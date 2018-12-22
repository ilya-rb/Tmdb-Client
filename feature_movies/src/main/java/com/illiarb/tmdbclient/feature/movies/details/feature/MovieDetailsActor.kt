package com.illiarb.tmdbclient.feature.movies.details.feature

import com.badoo.mvicore.element.Actor
import com.illiarb.tmdbclient.feature.movies.details.feature.MovieDetailsFeature.Action
import com.illiarb.tmdbclient.feature.movies.details.feature.MovieDetailsFeature.Effect
import com.illiarb.tmdblcient.core.modules.movie.MoviesInteractor
import com.illiarb.tmdblcient.core.system.SchedulerProvider
import io.reactivex.Observable
import javax.inject.Inject

/**
 * @author ilya-rb on 22.12.18.
 */
class MovieDetailsActor @Inject constructor(
    private val moviesInteractor: MoviesInteractor,
    private val schedulerProvider: SchedulerProvider
) : Actor<MovieDetailsState, Action, Effect> {

    override fun invoke(state: MovieDetailsState, action: Action): Observable<out Effect> =
        when (action) {
            is Action.LoadMovieDetailsAction -> fetchMovieDetails(action.id)
        }

    private fun fetchMovieDetails(id: Int): Observable<Effect.LoadMovieDetailsEffect> =
        moviesInteractor.getMovieDetails(id)
            .subscribeOn(schedulerProvider.provideIoScheduler())
            .observeOn(schedulerProvider.provideMainThreadScheduler())
            .toObservable()
            .map { Effect.LoadMovieDetailsEffect.Success(it) }
            .cast(Effect.LoadMovieDetailsEffect::class.java)
            .onErrorReturn(Effect.LoadMovieDetailsEffect::Failure)
            .startWith(Effect.LoadMovieDetailsEffect.InFlight)
}