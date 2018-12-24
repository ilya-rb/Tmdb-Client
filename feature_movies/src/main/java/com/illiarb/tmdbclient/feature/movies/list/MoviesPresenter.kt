package com.illiarb.tmdbclient.feature.movies.list

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.ext.ioToMain
import com.illiarb.tmdblcient.core.modules.movie.MoviesInteractor
import com.illiarb.tmdblcient.core.system.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * @author ilya-rb on 24.12.18.
 */
@InjectViewState
class MoviesPresenter @Inject constructor(
    private val moviesInteractor: MoviesInteractor,
    private val schedulerProvider: SchedulerProvider
) : MvpPresenter<MoviesView>() {

    private val destroyDisposable = CompositeDisposable()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        moviesInteractor.getMovieSections()
            .ioToMain(schedulerProvider)
            .doOnSubscribe { viewState.showProgress() }
            .doAfterTerminate { viewState.hideProgress() }
            .subscribe(
                { viewState.showMovieSections(it) },
                { viewState.showError(it.message ?: "") }
            )
            .addTo(destroyDisposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyDisposable.clear()
    }

    fun onMovieClicked(movie: Movie) {
        moviesInteractor.onMovieSelected(movie)
    }
}