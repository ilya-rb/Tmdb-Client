package com.illiarb.tmdblcient.core.storage

import com.illiarb.tmdblcient.core.domain.entity.Account
import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.common.NonBlocking

/**
 * @author ilya-rb on 21.11.18.
 */
interface AccountRepository {

    @NonBlocking
    suspend fun getCurrentAccount(): Account

    @NonBlocking
    suspend fun getRatedMovies(accountId: Int): List<Movie>

    @NonBlocking
    suspend fun getFavoriteMovies(accountId: Int): List<Movie>

    @NonBlocking
    suspend fun clearAccountData(): Boolean
}