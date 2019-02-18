package com.illiarb.tmdbclient.feature.home.details.presentation

import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.domain.MoviesInteractor
import com.illiarb.tmdblcient.core.domain.entity.Review
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
@ExperimentalCoroutinesApi
class MovieDetailsModel @Inject constructor(
    private val moviesInteractor: MoviesInteractor
) : BasePresentationModel<MovieDetailsUiState>(MovieDetailsUiState.idle()) {

    fun getMovieDetails(id: Int) = runCoroutine {
        setState { copy(isLoading = true) }

        handleResult(moviesInteractor.getMovieDetails(id), { movie ->
            setState {
                copy(isLoading = false, movie = movie)
            }
        })
    }

    fun onReviewsClicked(reviews: List<Review>) {
        executeAction(ShowReviewsAction(reviews))
    }
}