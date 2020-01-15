package com.illiarb.tmdblcient.core.interactor

import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieBlock
import com.illiarb.tmdblcient.core.domain.Video
import com.illiarb.tmdblcient.core.util.Result

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