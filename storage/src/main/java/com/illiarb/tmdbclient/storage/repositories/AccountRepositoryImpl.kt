package com.illiarb.tmdbclient.storage.repositories

import com.illiarb.tmdbclient.storage.local.PersistableStorage
import com.illiarb.tmdbclient.storage.mappers.AccountMapper
import com.illiarb.tmdbclient.storage.mappers.MovieMapper
import com.illiarb.tmdbclient.storage.network.api.service.AccountService
import com.illiarb.tmdblcient.core.domain.entity.Account
import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.storage.AccountRepository
import com.illiarb.tmdblcient.core.tools.DispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author ilya-rb on 21.11.18.
 */
class AccountRepositoryImpl @Inject constructor(
    private val accountService: AccountService,
    private val persistableStorage: PersistableStorage,
    private val accountMapper: AccountMapper,
    private val dispatcherProvider: DispatcherProvider,
    private val movieMapper: MovieMapper
) : AccountRepository {

    override suspend fun getCurrentAccount(): Account =
        withContext(dispatcherProvider.io) {
            val cachedAccount = persistableStorage.getCurrentAccount()
            if (cachedAccount.isNonExistent()) {
                val account = accountService.getAccountDetails(persistableStorage.getSessionId()).await()
                persistableStorage.storeAccount(account)

                accountMapper.map(account)
            } else {
                accountMapper.map(cachedAccount)
            }
        }

    override suspend fun getRatedMovies(accountId: Int): List<Movie> =
        withContext(dispatcherProvider.io) {
            val ratedMovies = accountService.getAccountRatedMovies(accountId, getSessionId()).await()
            movieMapper.mapList(ratedMovies.results)
        }

    override suspend fun getFavoriteMovies(accountId: Int): List<Movie> =
        withContext(dispatcherProvider.io) {
            val favoriteMovies = accountService.getAccountFavoriteMovies(accountId, getSessionId()).await()
            movieMapper.mapList(favoriteMovies.results)
        }

    override suspend fun clearAccountData(): Boolean =
        withContext(dispatcherProvider.io) {
            persistableStorage.clearAccountData()
            true
        }

    private fun getSessionId(): String = persistableStorage.getSessionId()
}