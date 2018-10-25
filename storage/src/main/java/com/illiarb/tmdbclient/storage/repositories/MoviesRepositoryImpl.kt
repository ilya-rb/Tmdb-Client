package com.illiarb.tmdbclient.storage.repositories

import com.illiarb.tmdblcient.core.modules.movie.MovieDataSource
import com.illiarb.tmdblcient.core.modules.movie.MoviesRepository
import javax.inject.Inject

/**
 * @author ilya-rb on 25.10.18.
 */
class MoviesRepositoryImpl @Inject constructor(private val remoteDataSource: MovieDataSource) : MoviesRepository by remoteDataSource