package com.illiarb.tmdbclient.services.tmdb.internal.interactor

import com.illiarb.tmdbclient.services.tmdb.domain.Genre
import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.interactor.GenresInteractor
import com.illiarb.tmdbclient.services.tmdb.internal.repository.GenresRepository
import javax.inject.Inject

internal class DefaultGenresInteractor @Inject constructor(
  private val genresRepository: GenresRepository
) : GenresInteractor {

  override suspend fun getAllGenres(): Result<List<Genre>> = genresRepository.getGenres()
}