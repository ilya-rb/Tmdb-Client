package com.tmdbclient.servicetmdb.internal.interactor

import com.tmdbclient.servicetmdb.domain.Genre
import com.tmdbclient.servicetmdb.domain.GenresSection
import com.tmdbclient.servicetmdb.domain.ListSection
import com.tmdbclient.servicetmdb.domain.MovieBlock
import com.tmdbclient.servicetmdb.domain.MovieSection
import com.tmdbclient.servicetmdb.domain.NowPlayingSection
import com.illiarb.tmdbclient.util.Result
import com.tmdbclient.servicetmdb.interactor.GenresInteractor
import com.tmdbclient.servicetmdb.interactor.HomeInteractor
import com.tmdbclient.servicetmdb.interactor.MoviesInteractor
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