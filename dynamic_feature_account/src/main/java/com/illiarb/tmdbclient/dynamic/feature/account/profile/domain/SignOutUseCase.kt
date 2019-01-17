package com.illiarb.tmdbclient.dynamic.feature.account.profile.domain

import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.domain.NonBlockingUseCase
import com.illiarb.tmdblcient.core.exception.ErrorHandler
import com.illiarb.tmdblcient.core.repository.AccountRepository
import com.illiarb.tmdblcient.core.system.coroutine.NonBlocking
import javax.inject.Inject

/**
 * @author ilya-rb on 10.01.19.
 */
class SignOutUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val errorHandler: ErrorHandler
) : NonBlockingUseCase<Boolean, Unit> {

    @NonBlocking
    override suspend fun executeAsync(payload: Unit): Result<Boolean> {
        return Result.create(errorHandler) {
            accountRepository.clearAccountData()
        }
    }
}