package com.illiarb.tmdbclient.movies.details

import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.domain.MoviesInteractor
import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.ext.launchAndCollect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
class MovieDetailsModel @Inject constructor(
    private val moviesInteractor: MoviesInteractor
) : BasePresentationModel() {

    private val movie = Channel<Movie>()

    fun bind(view: MovieDetailsView) {
        view.launchAndCollect(movie.consumeAsFlow()) { view.movie(it) }
    }

    fun getMovieDetails(id: Int) = launch {
        movie.send(moviesInteractor.getMovieDetails(id))
    }
}