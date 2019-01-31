package com.illiarb.tmdbclient.dynamic.feature.account.profile.presentation

import com.illiarb.tmdblcient.core.domain.entity.Account
import com.illiarb.tmdblcient.core.util.Cloneable

/**
 * @author ilya-rb on 24.12.18.
 */
data class AccountUiState(
    val isLoading: Boolean,
    val isBlockingLoading: Boolean,
    val account: Account?
) : Cloneable<AccountUiState> {

    companion object {
        fun idle() = AccountUiState(false, false, null)
    }

    override fun clone(): AccountUiState = copy()
}