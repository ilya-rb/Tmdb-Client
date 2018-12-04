package com.illiarb.tmdblcient.core.modules.account

import com.illiarb.tmdblcient.core.entity.Account
import io.reactivex.Single

/**
 * @author ilya-rb on 20.11.18.
 */
interface AccountInteractor {

    fun getCurrentAccount(): Single<Account>

    fun onLogoutClicked()
}