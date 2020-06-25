package com.illiarb.tmdbclient.services.tmdb.internal.interactor

import com.illiarb.tmdbclient.services.tmdb.api.domain.Genre
import com.illiarb.tmdbclient.services.tmdb.api.domain.GenresSection
import com.illiarb.tmdbclient.services.tmdb.api.domain.ListSection
import com.illiarb.tmdbclient.services.tmdb.api.domain.MovieBlock
import com.illiarb.tmdbclient.services.tmdb.api.domain.MovieSection
import com.illiarb.tmdbclient.services.tmdb.api.domain.NowPlayingSection
import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.api.interactor.GenresInteractor
import com.illiarb.tmdbclient.services.tmdb.api.interactor.HomeInteractor
import com.illiarb.tmdbclient.services.tmdb.api.interactor.MoviesInteractor
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

internal class DefaultHomeInteractor @Inject constructor(
  private val moviesInteractor: MoviesInteractor,
  private val genresInteractor: GenresInteractor
) : HomeInteractor {

  override suspend fun getHomeSections(): Result<List<MovieSection>> = coroutineScope {
    Result.create {
      val movies = async { moviesInteractor.getAllMovies().unwrap() }
      val genres = async {
        genresInteractor.getAllGenres()
          .unwrap()
          .map { it.copy(name = it.getNameWithEmoji()) }
      }
      createMovieSections(movies.await(), genres.await())
    }
  }

  private fun createMovieSections(
    movieBlocks: List<MovieBlock>,
    genres: List<Genre>
  ): List<MovieSection> {
    return movieBlocks
      .filter { it.movies.isNotEmpty() }
      .map {
        if (it.filter.isNowPlaying()) {
          NowPlayingSection(it.filter.name, it.movies)
        } else {
          ListSection(it.filter.code, it.filter.name, it.movies)
        }
      }
      .sortedByDescending {
        // Now playing section should always be on top
        it is NowPlayingSection
      }
      .toMutableList()
      .also {
        if (genres.isNotEmpty()) {
          if (genres.size > HomeInteractor.GENRES_MAX_SIZE) {
            it.add(1, GenresSection(genres.subList(0, HomeInteractor.GENRES_MAX_SIZE)))
          } else {
            it.add(1, GenresSection(genres))
          }
        }
      }
  }
}