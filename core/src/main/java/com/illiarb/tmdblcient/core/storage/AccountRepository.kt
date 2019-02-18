package com.illiarb.tmdblcient.core.storage

import com.illiarb.tmdblcient.core.domain.entity.Account
import com.illiarb.tmdblcient.core.domain.entity.Movie

/**
 * @author ilya-rb on 21.11.18.
 */
interface AccountRepository {

    suspend fun getCurrentAccount(): Account

    suspend fun getRatedMovies(accountId: Int): List<Movie>

    suspend fun getFavoriteMovies(accountId: Int): List<Movie>

    suspend fun clearAccountData(): Boolean
}