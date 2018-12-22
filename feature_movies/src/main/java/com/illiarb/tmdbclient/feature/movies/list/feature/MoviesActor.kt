package com.illiarb.tmdbclient.feature.movies.list.feature

import com.badoo.mvicore.element.Actor
import com.illiarb.tmdbclient.feature.movies.list.feature.MoviesFeature.Action
import com.illiarb.tmdbclient.feature.movies.list.feature.MoviesFeature.Effect
import com.illiarb.tmdblcient.core.modules.movie.MoviesInteractor
import com.illiarb.tmdblcient.core.system.SchedulerProvider
import io.reactivex.Observable
import javax.inject.Inject

/**
 * @author ilya-rb on 22.12.18.
 */
class MoviesActor @Inject constructor(
    private val moviesInteractor: MoviesInteractor,
    private val schedulersProvider: SchedulerProvider
) : Actor<MoviesState, MoviesFeature.Action, Effect> {

    override fun invoke(state: MoviesState, action: MoviesFeature.Action): Observable<out Effect> =
        when (action) {
            is Action.Refresh -> processRefreshAction()
            is Action.ShowMovieDetails -> Observable.just(Effect.ShowMovieDetails.Success(action.id))
        }

    private fun processRefreshAction(): Observable<out Effect> =
        moviesInteractor.getMovieSections()
            .toObservable()
            .subscribeOn(schedulersProvider.provideIoScheduler())
            .observeOn(schedulersProvider.provideMainThreadScheduler())
            .map { Effect.MoviesRefresh.Success(it) }
            .cast(Effect.MoviesRefresh::class.java)
            .onErrorReturn(Effect.MoviesRefresh::Failure)
            .startWith(Effect.MoviesRefresh.InFlight)
}