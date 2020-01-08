package com.illiarb.tmdblcient.core.interactor

import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.util.Result

interface HomeInteractor {

    suspend fun getHomeSections(): Result<List<MovieSection>>
}