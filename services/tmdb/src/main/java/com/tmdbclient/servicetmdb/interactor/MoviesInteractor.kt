package com.tmdbclient.servicetmdb.interactor

import com.tmdbclient.servicetmdb.domain.Movie
import com.tmdbclient.servicetmdb.domain.MovieBlock
import com.tmdbclient.servicetmdb.domain.Video
import com.illiarb.tmdbclient.util.Result

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