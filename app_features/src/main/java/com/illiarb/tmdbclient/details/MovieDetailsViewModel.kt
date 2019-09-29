package com.illiarb.tmdbclient.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.services.TmdbService
import com.illiarb.tmdblcient.core.util.Async
import com.illiarb.tmdblcient.core.util.Either
import com.illiarb.tmdblcient.core.util.Fail
import com.illiarb.tmdblcient.core.util.Success
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface MovieDetailsViewModel {

    val movie: LiveData<Async<Movie>>

    class DefaultDetailsViewModel @Inject constructor(
        private val tmdbService: TmdbService,
        private val movieId: Int
    ) : BasePresentationModel(), MovieDetailsViewModel {

        private val _movie = liveData {
            tmdbService.getMovieDetails(movieId)
                .map {
                    when (it) {
                        is Either.Left -> Success(it.data)
                        is Either.Right -> Fail<Movie>(it.error)
                    }
                }
                .collect { emit(it) }
        }

        override val movie: LiveData<Async<Movie>>
            get() = _movie

    }
}