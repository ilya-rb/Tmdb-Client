package com.illiarb.tmdbclient.dynamic.feature.account.profile.presentation

import com.illiarb.tmdblcient.core.domain.entity.Account
import com.illiarb.tmdblcient.core.util.Cloneable

/**
 * @author ilya-rb on 24.12.18.
 */
data class AccountUiState(
    val isLoading: Boolean = false,
    val isBlockingLoading: Boolean = false,
    val account: Account? = null
) : Cloneable<AccountUiState> {

    override fun clone(): AccountUiState = copy()
}