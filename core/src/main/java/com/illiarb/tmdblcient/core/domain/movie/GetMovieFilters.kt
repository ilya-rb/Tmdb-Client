package com.illiarb.tmdblcient.core.domain.movie

import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.system.NonBlocking

/**
 * @author ilya-rb on 07.01.19.
 */
interface GetMovieFilters {

    @NonBlocking
    suspend operator fun invoke(): List<MovieFilter>
}