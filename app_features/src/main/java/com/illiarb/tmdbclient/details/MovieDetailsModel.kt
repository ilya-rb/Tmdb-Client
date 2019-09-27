package com.illiarb.tmdbclient.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.services.TmdbService
import com.illiarb.tmdblcient.core.util.Async
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
class MovieDetailsModel @Inject constructor(
    private val tmdbService: TmdbService,
    private val movieId: Int
) : BasePresentationModel(), MovieDetailsViewModel {

    private val _movie = liveData {
        tmdbService.getMovieDetails(movieId).collect {
            emit(it)
        }
    }

    override val movie: LiveData<Async<Movie>>
        get() = _movie
}