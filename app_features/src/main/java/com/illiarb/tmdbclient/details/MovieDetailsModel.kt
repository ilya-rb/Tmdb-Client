package com.illiarb.tmdbclient.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.illiarb.tmdbclient.details.MovieDetailsModel.MovieInfo
import com.illiarb.tmdbclient.details.MovieDetailsModel.MoviePhotos
import com.illiarb.tmdbclient.details.MovieDetailsModel.MovieSimilar
import com.illiarb.tmdbclient.details.MovieDetailsModel.UiEvent
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.analytics.AnalyticEvent.RouterAction
import com.illiarb.tmdblcient.core.analytics.AnalyticsService
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.interactor.MoviesInteractor
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowMovieDetails
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowVideos
import com.illiarb.tmdblcient.core.util.Async
import com.illiarb.tmdblcient.core.util.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

interface MovieDetailsModel {

    val movie: LiveData<Async<Movie>>

    val movieSections: LiveData<List<Any>>

    fun onUiEvent(event: UiEvent)

    data class MovieInfo(val movie: Movie)
    data class MoviePhotos(val movie: Movie)
    data class MovieSimilar(val movies: List<Movie>)

    sealed class UiEvent {
        object PlayClicked : UiEvent()
        class MovieClicked(val movie: Movie) : UiEvent()
    }
}

class DefaultDetailsViewModel @Inject constructor(
    private val movieId: Int,
    private val moviesInteractor: MoviesInteractor,
    private val router: Router,
    private val analyticsService: AnalyticsService
) : BasePresentationModel(), MovieDetailsModel {

    private val movieLiveData = flow { emit(moviesInteractor.getMovieDetails(movieId)) }
        .map { it.asAsync() }
        .catch { emit(Async.Fail(it)) }
        .onStart { emit(Async.Loading()) }
        .asLiveData()

    private val movieSectionsData = MediatorLiveData<List<Any>>().apply {
        addSource(movieLiveData) {
            if (it !is Async.Success) {
                return@addSource
            }

            val movie = it()
            val movieInfo = MovieInfo(movie)
            val moviePhotos = MoviePhotos(movie)

            value = listOf(movieInfo, moviePhotos)

            viewModelScope.launch {
                val similar = moviesInteractor.getSimilarMovies(movieId)
                if (similar is Result.Success) {
                    value = value!!.toMutableList().plus(MovieSimilar(similar.data))
                }
            }
        }
    }

    override val movie: LiveData<Async<Movie>>
        get() = movieLiveData

    override val movieSections: LiveData<List<Any>>
        get() = movieSectionsData

    override fun onUiEvent(event: UiEvent) {
        when (event) {
            is UiEvent.MovieClicked -> {
                val action = ShowMovieDetails(event.movie.id)
                analyticsService.trackEvent(RouterAction(action))
                router.executeAction(action)
            }
            is UiEvent.PlayClicked -> {
                val action = ShowVideos(movieId)
                analyticsService.trackEvent(RouterAction(action))
                router.executeAction(action)
            }
        }
    }
}