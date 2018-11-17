package com.illiarb.tmdbclient.storage.local.movies

import com.illiarb.tmdbclient.storage.local.PersistableStorage
import com.illiarb.tmdbclient.storage.model.MovieListModel
import com.illiarb.tmdbclient.storage.model.MovieModel
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author ilya-rb on 26.10.18.
 */
class MoviesStorage @Inject constructor(private val persistableStorage: PersistableStorage) {

    fun getMoviesByType(type: String): Single<List<MovieModel>> {
        val movies = persistableStorage.getValue(type, MovieListModel()).movies
        return Single.just(movies)
    }

    fun storeMovies(type: String, movies: List<MovieModel>) {
        persistableStorage.putValue(type, MovieListModel(movies))
    }
}