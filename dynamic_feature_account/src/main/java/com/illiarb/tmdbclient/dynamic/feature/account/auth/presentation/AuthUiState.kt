package com.illiarb.tmdbclient.dynamic.feature.account.auth.presentation

import com.illiarb.tmdbexplorer.coreui.observable.Cloneable

/**
 * @author ilya-rb on 23.12.18.
 */
data class AuthUiState(
    val isLoading: Boolean,
    val error: Throwable?,
    val authButtonEnabled: Boolean
) : Cloneable<AuthUiState> {

    companion object {
        fun idle() = AuthUiState(false, null, false)
    }

    override fun clone(): AuthUiState = copy()
}