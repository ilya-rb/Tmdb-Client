package com.illiarb.tmdbclient.storage.repositories

import com.illiarb.tmdbclient.storage.local.PersistableStorage
import com.illiarb.tmdblcient.core.storage.AccountRepository
import javax.inject.Inject

/**
 * @author ilya-rb on 20.11.18.
 */
class AccountRepositoryImpl @Inject constructor(
    private val persistableStorage: PersistableStorage
) : AccountRepository {

    override fun isAuthorized(): Boolean = persistableStorage.isAuthorized()
}