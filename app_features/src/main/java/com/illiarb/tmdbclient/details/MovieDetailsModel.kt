package com.illiarb.tmdbclient.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.services.TmdbService
import com.illiarb.tmdblcient.core.util.Async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

interface MovieDetailsModel {

    val movie: LiveData<Async<Movie>>

    fun onUiEvent(event: UiEvent)

    sealed class UiEvent {
        object PlayClicked : UiEvent()
    }
}

class DefaultDetailsViewModel @Inject constructor(
    private val movieId: Int,
    private val tmdbService: TmdbService
) : BasePresentationModel(), MovieDetailsModel {

    private val _movie = flow { emit(tmdbService.getMovieDetails(movieId)) }
        .map { it.asAsync() }
        .catch { emit(Async.Fail(it)) }
        .onStart { emit(Async.Loading()) }
        .asLiveData()

    override val movie: LiveData<Async<Movie>>
        get() = _movie

    override fun onUiEvent(event: MovieDetailsModel.UiEvent) = Unit
}