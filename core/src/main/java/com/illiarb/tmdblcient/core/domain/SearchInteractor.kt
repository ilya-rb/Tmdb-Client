package com.illiarb.tmdblcient.core.domain

import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.domain.entity.Movie

/**
 * @author ilya-rb on 18.02.19.
 */
interface SearchInteractor {

    suspend fun searchMovies(query: String): Result<List<Movie>>
}