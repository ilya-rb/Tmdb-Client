package com.illiarb.tmdbclient.feature.home.list

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.ext.ioToMain
import com.illiarb.tmdblcient.core.modules.movie.HomeInteractor
import com.illiarb.tmdblcient.core.system.SchedulerProvider
import io.reactivex.SingleTransformer
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * @author ilya-rb on 24.12.18.
 */
@InjectViewState
class HomePresenter @Inject constructor(
    private val homeInteractor: HomeInteractor,
    private val schedulerProvider: SchedulerProvider
) : MvpPresenter<MoviesView>() {

    private val destroyDisposable = CompositeDisposable()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        homeInteractor.getMovieFilters()
            .ioToMain(schedulerProvider)
            .compose(progressTransformer())
            .subscribe(
                { viewState.showMovieFilters(it) },
                { viewState.showError(it.message ?: "") }
            )
            .addTo(destroyDisposable)

        homeInteractor.getMovieSections()
            .ioToMain(schedulerProvider)
            .compose(progressTransformer())
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

    fun onSearchClicked() {
        homeInteractor.onSearchClicked()
    }

    fun onAccountClicked() {
        homeInteractor.onAccountClicked()
    }

    fun onMovieClicked(movie: Movie) {
        homeInteractor.onMovieSelected(movie)
    }

    fun onFilterSelected(filter: MovieFilter) {

    }

    private fun <T> progressTransformer(): SingleTransformer<T, T> =
        SingleTransformer {
            it.doOnSubscribe { viewState.showProgress() }.doAfterTerminate { viewState.hideProgress() }
        }
}