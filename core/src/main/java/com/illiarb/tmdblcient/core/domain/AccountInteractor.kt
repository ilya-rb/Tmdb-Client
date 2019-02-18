package com.illiarb.tmdblcient.core.domain

import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.domain.entity.Account

/**
 * @author ilya-rb on 18.02.19.
 */
interface AccountInteractor {

    suspend fun getAccount(): Result<Account>

    suspend fun exitFromAccount(): Result<Unit>

}