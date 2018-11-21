package com.illiarb.tmdbclient.coreimpl.account

import com.illiarb.tmdblcient.core.entity.Account
import com.illiarb.tmdblcient.core.modules.account.AccountInteractor
import com.illiarb.tmdblcient.core.modules.account.AccountRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author ilya-rb on 21.11.18.
 */
class AccountInteractorImpl @Inject constructor(
    private val accountRepository: AccountRepository
) : AccountInteractor {

    override fun getCurrentAccount(): Single<Account> = accountRepository.getCurrentAccount()
}