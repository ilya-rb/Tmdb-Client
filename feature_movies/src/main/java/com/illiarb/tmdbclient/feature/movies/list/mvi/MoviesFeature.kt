package com.illiarb.tmdbclient.feature.movies.list.mvi

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.element.WishToAction
import com.badoo.mvicore.feature.BaseFeature
import com.illiarb.tmdblcient.core.entity.MovieSection
import com.illiarb.tmdblcient.core.modules.movie.MoviesInteractor
import com.illiarb.tmdblcient.core.system.SchedulerProvider
import io.reactivex.Observable
import java.util.Collections
import javax.inject.Inject

/**
 * @author ilya-rb on 22.12.18.
 */
class MoviesFeature @Inject constructor(
    moviesActor: MoviesActor,
    moviesWithToAction: MoviesWithToAction,
    reducer: MoviesReducer,
    newsPublisher: MoviesNewsPublisher
) : BaseFeature<MoviesWish, MoviesAction, MoviesEffect, MoviesState, MoviesNews>(
    initialState = MoviesState.idle(),
    wishToAction = moviesWithToAction,
    actor = moviesActor,
    reducer = reducer,
    newsPublisher = newsPublisher
) {

    class MoviesWithToAction @Inject constructor() : WishToAction<MoviesWish, MoviesAction> {
        override fun invoke(wish: MoviesWish): MoviesAction =
            when (wish) {
                MoviesWish.Refresh -> MoviesAction.RefreshAction
                is MoviesWish.ShowMovieDetails -> MoviesAction.ShowMovieDetailsAction(wish.id)
            }
    }

    class MoviesActor @Inject constructor(
        private val moviesInteractor: MoviesInteractor,
        private val schedulersProvider: SchedulerProvider
    ) : Actor<MoviesState, MoviesAction, MoviesEffect> {

        override fun invoke(state: MoviesState, action: MoviesAction): Observable<out MoviesEffect> =
            when (action) {
                MoviesAction.RefreshAction ->
                    moviesInteractor.getMovieSections()
                        .toObservable()
                        .subscribeOn(schedulersProvider.provideIoScheduler())
                        .observeOn(schedulersProvider.provideMainThreadScheduler())
                        .map { MoviesEffect.MoviesRefreshEffect.Success(it) }
                        .cast(MoviesEffect.MoviesRefreshEffect::class.java)
                        .onErrorReturn(MoviesEffect.MoviesRefreshEffect::Failure)
                        .startWith(MoviesEffect.MoviesRefreshEffect.InFlight)

                is MoviesAction.ShowMovieDetailsAction ->
                    Observable.just(MoviesEffect.ShowMovieDetailsEffect.Success(action.id))
            }
    }

    class MoviesReducer @Inject constructor() : Reducer<MoviesState, MoviesEffect> {
        override fun invoke(state: MoviesState, effect: MoviesEffect): MoviesState =
            when (effect) {
                MoviesEffect.MoviesRefreshEffect.InFlight -> state.copy(isLoading = true)

                is MoviesEffect.MoviesRefreshEffect.Success ->
                    state.copy(
                        isLoading = false,
                        movies = effect.movies,
                        error = null
                    )

                is MoviesEffect.MoviesRefreshEffect.Failure ->
                    state.copy(
                        isLoading = false,
                        error = effect.error
                    )

                is MoviesEffect.ShowMovieDetailsEffect.Success -> state
            }
    }

    class MoviesNewsPublisher @Inject constructor() : NewsPublisher<MoviesAction, MoviesEffect, MoviesState, MoviesNews> {
        override fun invoke(action: MoviesAction, effect: MoviesEffect, state: MoviesState): MoviesNews? =
            when (effect) {
                is MoviesEffect.ShowMovieDetailsEffect.Success -> MoviesNews.ShowMovieDetails(effect.id)
                else -> null
            }
    }
}

sealed class MoviesWish {
    object Refresh : MoviesWish()
    data class ShowMovieDetails(val id: Int) : MoviesWish()
}

sealed class MoviesAction {
    object RefreshAction : MoviesAction()
    data class ShowMovieDetailsAction(val id: Int) : MoviesAction()
}

sealed class MoviesEffect {

    sealed class MoviesRefreshEffect : MoviesEffect() {
        object InFlight : MoviesRefreshEffect()
        data class Success(val movies: List<MovieSection>) : MoviesRefreshEffect()
        data class Failure(val error: Throwable) : MoviesRefreshEffect()
    }

    sealed class ShowMovieDetailsEffect : MoviesEffect() {
        data class Success(val id: Int) : ShowMovieDetailsEffect()
    }
}

sealed class MoviesNews {
    data class ShowMovieDetails(val id: Int) : MoviesNews()
}

data class MoviesState(
    val isLoading: Boolean = false,
    val movies: List<MovieSection> = Collections.emptyList(),
    val error: Throwable? = null
) {
    companion object {
        fun idle() = MoviesState(false)
    }
}