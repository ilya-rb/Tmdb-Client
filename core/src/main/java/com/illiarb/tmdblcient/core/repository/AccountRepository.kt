package com.illiarb.tmdblcient.core.repository

import com.illiarb.tmdblcient.core.entity.Account
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.system.coroutine.NonBlocking

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