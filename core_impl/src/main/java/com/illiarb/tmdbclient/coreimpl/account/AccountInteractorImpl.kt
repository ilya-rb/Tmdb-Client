package com.illiarb.tmdbclient.coreimpl.account

import com.illiarb.tmdblcient.core.entity.Account
import com.illiarb.tmdblcient.core.modules.account.AccountInteractor
import com.illiarb.tmdblcient.core.modules.account.AccountRepository
import com.illiarb.tmdblcient.core.navigation.AuthScreen
import com.illiarb.tmdblcient.core.navigation.Router
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author ilya-rb on 21.11.18.
 */
class AccountInteractorImpl @Inject constructor(
    private val accountRepository: AccountRepository,
    private val router: Router
) : AccountInteractor {

    override fun getCurrentAccount(): Single<Account> = accountRepository.getCurrentAccount()

    override fun onLogoutClicked() {
        accountRepository.clearAccountData()
            .andThen { router.navigateTo(AuthScreen) }
            .subscribe()
    }
}