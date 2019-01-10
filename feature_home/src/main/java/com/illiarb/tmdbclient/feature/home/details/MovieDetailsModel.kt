package com.illiarb.tmdbclient.feature.home.details

import com.illiarb.tmdbclient.feature.home.domain.GetMovieDetails
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
class MovieDetailsModel @Inject constructor(
    private val getMovieDetails: GetMovieDetails
) : BasePresentationModel<MovieDetailsUiState>() {

    fun getMovieDetails(id: Int) = launch(context = coroutineContext) {
        setState(MovieDetailsUiState(true, null))

        try {
            val movie = getMovieDetails.execute(id)
            setState(MovieDetailsUiState(false, movie))
        } catch (e: Exception) {
            // Process error
        }
    }
}