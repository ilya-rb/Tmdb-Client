package com.illiarb.tmdblcient.core.domain.profile

import com.illiarb.tmdblcient.core.entity.Account
import com.illiarb.tmdblcient.core.modules.account.AccountRepository
import com.illiarb.tmdblcient.core.system.NonBlocking
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * @author ilya-rb on 07.01.19.
 */
class GetProfileUseCaseImpl @Inject constructor(
    private val accountRepository: AccountRepository
) : GetProfileUseCase {

    @NonBlocking
    override suspend fun invoke(): Account = coroutineScope {
        accountRepository.getCurrentAccount()
    }
}