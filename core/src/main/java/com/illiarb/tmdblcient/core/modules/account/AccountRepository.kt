package com.illiarb.tmdblcient.core.modules.account

import com.illiarb.tmdblcient.core.entity.Account
import io.reactivex.Completable
import io.reactivex.Single

/**
 * @author ilya-rb on 21.11.18.
 */
interface AccountRepository {

    fun getCurrentAccount(): Single<Account>

    fun clearAccountData(): Completable
}