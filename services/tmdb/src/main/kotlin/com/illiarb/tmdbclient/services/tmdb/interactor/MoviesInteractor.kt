package com.illiarb.tmdbclient.services.tmdb.interactor

import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.domain.MovieBlock
import com.illiarb.tmdbclient.services.tmdb.domain.Video
import com.illiarb.tmdbclient.libs.util.Result

interface MoviesInteractor {

  companion object {
    const val KEY_INCLUDE_IMAGES = "images"
    const val KEY_INCLUDE_VIDEOS = "videos"
  }

  suspend fun getAllMovies(): Result<List<MovieBlock>>

  suspend fun getMovieDetails(movieId: Int): Result<Movie>

  suspend fun getMovieVideos(movieId: Int): Result<List<Video>>

  suspend fun getSimilarMovies(movieId: Int): Result<List<Movie>>

  suspend fun discoverMovies(genreId: Int): Result<List<Movie>>
}