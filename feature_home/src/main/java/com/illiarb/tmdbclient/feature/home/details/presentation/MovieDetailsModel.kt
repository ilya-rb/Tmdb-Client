package com.illiarb.tmdbclient.feature.home.details.presentation

import com.illiarb.tmdbclient.feature.home.details.domain.GetMovieDetails
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
@ExperimentalCoroutinesApi
class MovieDetailsModel @Inject constructor(
    private val getMovieDetails: GetMovieDetails
) : BasePresentationModel<MovieDetailsUiState>() {

    init {
        setIdleState(MovieDetailsUiState.idle())
    }

    fun getMovieDetails(id: Int) = launch(context = coroutineContext) {
        setState { it.copy(isLoading = true) }

        handleResult(getMovieDetails.executeAsync(id), { movie ->
            setState {
                it.copy(isLoading = false, movie = movie)
            }
        })
    }
}