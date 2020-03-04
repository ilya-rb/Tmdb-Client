package com.tmdbclient.servicetmdb.interactor

import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.domain.GenresSection
import com.illiarb.tmdblcient.core.domain.ListSection
import com.illiarb.tmdblcient.core.domain.MovieBlock
import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.domain.NowPlayingSection
import com.illiarb.tmdblcient.core.interactor.GenresInteractor
import com.illiarb.tmdblcient.core.interactor.HomeInteractor
import com.illiarb.tmdblcient.core.interactor.MoviesInteractor
import com.illiarb.tmdblcient.core.util.Result
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class DefaultHomeInteractor @Inject constructor(
  private val moviesInteractor: MoviesInteractor,
  private val genresInteractor: GenresInteractor
) : HomeInteractor {

  override suspend fun getHomeSections(): Result<List<MovieSection>> = coroutineScope {
    Result.create {
      val movies = async { moviesInteractor.getAllMovies().getOrThrow() }
      val genres = async {
        genresInteractor.getAllGenres()
          .getOrThrow()
          .map { it.copy(name = it.getNameWithEmoji()) }
      }
      createMovieSections(movies.await(), genres.await())
    }
  }

  private fun createMovieSections(movieBlocks: List<MovieBlock>, genres: List<Genre>): List<MovieSection> {
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