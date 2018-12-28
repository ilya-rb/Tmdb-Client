package com.illiarb.tmdbclient.feature.home.details

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.ext.ioToMain
import com.illiarb.tmdblcient.core.modules.movie.MovieDetailsInteractor
import com.illiarb.tmdblcient.core.system.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * @author ilya-rb on 24.12.18.
 */
@InjectViewState
class MovieDetailsPresenter @Inject constructor(
    private val movieDetailsInteractor: MovieDetailsInteractor,
    private val schedulerProvider: SchedulerProvider
) : MvpPresenter<MovieDetailsView>() {

    var id: Int = 0
        set(value) {
            field = value
            if (value > 0) {
                getMovieDetails(value)
            }
        }

    private val destroyDisposable = CompositeDisposable()

    private fun getMovieDetails(id: Int) {
        movieDetailsInteractor.getMovieDetails(id)
            .ioToMain(schedulerProvider)
            .subscribe(
                { viewState.showMovieDetails(it) },
                { viewState.showError(it.message ?: "") }
            )
            .addTo(destroyDisposable)
    }

    fun onGenreClicked(id: Int) {
        movieDetailsInteractor.onGenreSelected(id)
    }
}