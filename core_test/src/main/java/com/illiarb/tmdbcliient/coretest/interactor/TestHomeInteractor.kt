package com.illiarb.tmdbcliient.coretest.interactor

import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.interactor.HomeInteractor
import com.illiarb.tmdblcient.core.util.Result
import java.util.Collections

class TestHomeInteractor : HomeInteractor {

  override suspend fun getHomeSections(): Result<List<MovieSection>> {
    return Result.Success(Collections.emptyList())
  }
}