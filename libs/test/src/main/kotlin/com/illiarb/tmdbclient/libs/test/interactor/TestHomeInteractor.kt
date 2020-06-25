package com.illiarb.tmdbclient.libs.test.interactor

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.api.domain.MovieSection
import com.illiarb.tmdbclient.services.tmdb.api.interactor.HomeInteractor
import java.util.Collections

class TestHomeInteractor : HomeInteractor {

  override suspend fun getHomeSections(): Result<List<MovieSection>> {
    return Result.Ok(Collections.emptyList())
  }
}