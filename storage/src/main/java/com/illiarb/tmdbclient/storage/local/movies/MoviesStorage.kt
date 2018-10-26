package com.illiarb.tmdbclient.storage.local.movies

import com.illiarb.tmdbclient.storage.dto.MovieDto
import com.illiarb.tmdbclient.storage.dto.MovieListDto
import com.illiarb.tmdbclient.storage.local.PersistableStorage
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author ilya-rb on 26.10.18.
 */
class MoviesStorage @Inject constructor(private val persistableStorage: PersistableStorage) {

    fun getMoviesByType(type: String): Single<List<MovieDto>> {
        val movies = persistableStorage.getValue(type, MovieListDto()).movies
        return Single.just(movies)
    }

    fun storeMovies(type: String, movies: List<MovieDto>) {
        persistableStorage.putValue(type, MovieListDto(movies))
    }
}