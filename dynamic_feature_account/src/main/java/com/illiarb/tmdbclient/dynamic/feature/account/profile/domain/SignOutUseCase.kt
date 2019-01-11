package com.illiarb.tmdbclient.dynamic.feature.account.profile.domain

import com.illiarb.tmdblcient.core.domain.UseCase
import com.illiarb.tmdblcient.core.repository.AccountRepository
import com.illiarb.tmdblcient.core.system.NonBlocking
import javax.inject.Inject

/**
 * @author ilya-rb on 10.01.19.
 */
class SignOutUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) : UseCase<Boolean, Unit> {

    @NonBlocking
    override suspend fun execute(payload: Unit): Boolean {
        return accountRepository.clearAccountData()
    }
}