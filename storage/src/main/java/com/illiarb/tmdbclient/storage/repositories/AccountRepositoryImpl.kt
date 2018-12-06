package com.illiarb.tmdbclient.storage.repositories

import com.illiarb.tmdbclient.storage.local.PersistableStorage
import com.illiarb.tmdbclient.storage.mappers.AccountMapper
import com.illiarb.tmdbclient.storage.mappers.MovieMapper
import com.illiarb.tmdbclient.storage.network.api.service.AccountService
import com.illiarb.tmdblcient.core.entity.Account
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.modules.account.AccountRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author ilya-rb on 21.11.18.
 */
class AccountRepositoryImpl @Inject constructor(
    private val accountService: AccountService,
    private val persistableStorage: PersistableStorage,
    private val accountMapper: AccountMapper,
    private val movieMapper: MovieMapper
) : AccountRepository {

    override fun getCurrentAccount(): Single<Account> =
        persistableStorage.getCurrentAccount()
            .flatMap { cachedAccount ->
                if (cachedAccount.isNonExistent()) {
                    accountService.getAccountDetails(persistableStorage.getSessionId())
                        .doOnSuccess { accountFromService -> persistableStorage.storeAccount(accountFromService) }
                } else {
                    Single.just(cachedAccount)
                }
            }
            .map(accountMapper::map)

    override fun getRatedMovies(accountId: Int): Single<List<Movie>> =
        accountService.getAccountRatedMovies(accountId, getSessionId())
            .map { it.results }
            .map { movieMapper.mapList(it) }

    override fun getFavoriteMovies(accountId: Int): Single<List<Movie>> =
        accountService.getAccountFavoriteMovies(accountId, getSessionId())
            .map { it.results }
            .map { movieMapper.mapList(it) }

    override fun clearAccountData(): Completable = Completable.fromAction {
        persistableStorage.clearAccountData()
    }

    private fun getSessionId(): String = persistableStorage.getSessionId()
}