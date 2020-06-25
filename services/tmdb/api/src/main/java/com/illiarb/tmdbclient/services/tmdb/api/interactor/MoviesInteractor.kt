package com.illiarb.tmdbclient.services.tmdb.api.interactor

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.api.domain.Filter
import com.illiarb.tmdbclient.services.tmdb.api.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.api.domain.MovieBlock
import com.illiarb.tmdbclient.services.tmdb.api.domain.PagedList
import com.illiarb.tmdbclient.services.tmdb.api.domain.Video

interface MoviesInteractor {

  companion object {
    const val KEY_INCLUDE_IMAGES = "images"
    const val KEY_INCLUDE_VIDEOS = "videos"
  }

  suspend fun getAllMovies(): Result<List<MovieBlock>>

  suspend fun getMovieDetails(movieId: Int): Result<Movie>

  suspend fun getMovieVideos(movieId: Int): Result<List<Video>>

  suspend fun getSimilarMovies(movieId: Int): Result<List<Movie>>

  suspend fun discoverMovies(filter: Filter, page: Int): Result<PagedList<Movie>>

  suspend fun searchMovies(query: String): Result<PagedList<Movie>>
}