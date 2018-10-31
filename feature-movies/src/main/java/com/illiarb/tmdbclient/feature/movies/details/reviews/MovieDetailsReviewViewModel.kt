package com.illiarb.tmdbclient.feature.movies.details.reviews

import com.illiarb.tmdbexplorer.coreui.state.DataUiStateSubject
import com.illiarb.tmdbexplorer.coreui.state.UiState
import com.illiarb.tmdbexplorer.coreui.state.subscribe
import com.illiarb.tmdbexplorer.coreui.viewmodel.BaseViewModel
import com.illiarb.tmdblcient.core.entity.Review
import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.ext.ioToMain
import com.illiarb.tmdblcient.core.modules.movie.MoviesInteractor
import com.illiarb.tmdblcient.core.system.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * @author ilya-rb on 18.10.18.
 */
class MovieDetailsReviewViewModel @Inject constructor(
    private val moviesInteractor: MoviesInteractor,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private val movieReviewsSubject = object : DataUiStateSubject<Int, List<Review>>() {
        override fun createData(payload: Int): Disposable =
            moviesInteractor.getMovieReviews(payload)
                .ioToMain(schedulerProvider)
                .subscribe(this)
                .addTo(clearDisposable)
    }

    fun observeMovieReviews(): Observable<UiState<List<Review>>> = movieReviewsSubject.observer()

    fun fetchReviews(id: Int) {
        if (id != 0 && !movieReviewsSubject.hasData()) {
            movieReviewsSubject.loadData(id)
        }
    }
}