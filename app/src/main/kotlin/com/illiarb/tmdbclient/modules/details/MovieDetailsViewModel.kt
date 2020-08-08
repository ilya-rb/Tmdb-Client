package com.illiarb.tmdbclient.modules.details

import androidx.lifecycle.viewModelScope
import com.illiarb.tmdbclient.libs.ui.base.viewmodel.BaseViewModel
import com.illiarb.tmdbclient.libs.ui.common.ErrorMessage
import com.illiarb.tmdbclient.libs.ui.common.ViewStateEvent
import com.illiarb.tmdbclient.libs.util.Async
import com.illiarb.tmdbclient.modules.details.MovieDetailsViewModel.Event
import com.illiarb.tmdbclient.modules.details.MovieDetailsViewModel.State
import com.illiarb.tmdbclient.navigation.NavigationAction.MovieDetails.GoToMovieDetails
import com.illiarb.tmdbclient.navigation.NavigationAction.MovieDetails.GoToVideos
import com.illiarb.tmdbclient.navigation.Router
import com.illiarb.tmdbclient.services.analytics.AnalyticsService
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.interactor.MoviesInteractor
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(
  private val movieId: Int,
  private val moviesInteractor: MoviesInteractor,
  private val router: Router,
  private val analyticsService: AnalyticsService
) : BaseViewModel<State, Event>(initialState()) {

  companion object {
    private fun initialState() = State(
      movie = Async.Loading(),
      movieSections = emptyList(),
      error = null
    )
  }

  init {
    viewModelScope.launch {
      when (val result = moviesInteractor.getMovieDetails(movieId).asAsync()) {
        is Async.Success -> {
          val movie = result()
          val sections = mutableListOf<MovieDetailsSection>().apply {
            add(MovieDetailsSection.MovieInfo(movie))

            if (movie.images.isNotEmpty()) {
              add(MovieDetailsSection.MoviePhotos(movie))
            }

            val similar = moviesInteractor.getSimilarMovies(movieId)
            similar.doIfOk {
              if (it.isNotEmpty()) {
                add(MovieDetailsSection.MovieSimilar(it))
              }
            }
          }

          setState {
            copy(movie = result, movieSections = sections)
          }
        }
        is Async.Fail -> {
          setState {
            copy(
              movie = result,
              error = result.errorOrNull()?.let { error ->
                ViewStateEvent(ErrorMessage(error.message ?: ""))
              }
            )
          }
        }
      }
    }
  }

  override fun onUiEvent(event: Event) {
    when (event) {
      is Event.MovieClicked -> router.executeAction(GoToMovieDetails(event.movie.id))
      is Event.PlayClicked -> router.executeAction(GoToVideos(movieId))
    }
  }

  data class State(
    val movie: Async<Movie>,
    val movieSections: List<MovieDetailsSection>,
    val error: ViewStateEvent<ErrorMessage>?
  )

  sealed class MovieDetailsSection {
    data class MovieInfo(val movie: Movie) : MovieDetailsSection()
    data class MoviePhotos(val movie: Movie) : MovieDetailsSection()
    data class MovieSimilar(val movies: List<Movie>) : MovieDetailsSection()
  }

  sealed class Event {
    object PlayClicked : Event()
    class MovieClicked(val movie: Movie) : Event()
  }
}