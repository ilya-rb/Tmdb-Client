package com.illiarb.tmdbclient.details.presentation

import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.domain.MoviesInteractor
import com.illiarb.tmdblcient.core.domain.entity.Movie
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
@FlowPreview
@InternalCoroutinesApi
class MovieDetailsModel @Inject constructor(
    private val moviesInteractor: MoviesInteractor
) : BasePresentationModel() {

    private val movie = Channel<Movie>()

    fun bind(view: MovieDetailsView) {
        view.viewScope.launch {
            movie.consumeAsFlow().collect(view.movieCollector)

            view.reviewsClick().collect {
                // TODO: Navigate to reviews
            }
        }
    }

    fun getMovieDetails(id: Int) = launch {
        // TODO: Show progress

        moviesInteractor.getMovieDetails(id).onSuccess {
            movie.send(it)
        }

        // TODO: Hide progress
    }
}