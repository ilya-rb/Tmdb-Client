package com.illiarb.tmdbclient.dynamic.feature.account.profile.presentation

import com.illiarb.tmdbexplorer.coreui.observable.Cloneable
import com.illiarb.tmdblcient.core.entity.Account

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