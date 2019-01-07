package com.illiarb.tmdbclient.dynamic.feature.account.profile

import com.illiarb.tmdblcient.core.entity.Account

/**
 * @author ilya-rb on 24.12.18.
 */
data class AccountState(
    val isLoading: Boolean = false,
    val isBlockingLoading: Boolean = false,
    val account: Account? = null
) {

    companion object {
        fun idle() = AccountState()
    }
}