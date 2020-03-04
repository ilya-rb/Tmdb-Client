package com.illiarb.tmdblcient.core.interactor

import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.util.Result

interface GenresInteractor {

  suspend fun getAllGenres(): Result<List<Genre>>
}