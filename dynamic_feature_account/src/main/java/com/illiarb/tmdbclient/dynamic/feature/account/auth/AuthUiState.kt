package com.illiarb.tmdbclient.dynamic.feature.account.auth

import com.illiarb.tmdbexplorer.coreui.Cloneable

/**
 * @author ilya-rb on 23.12.18.
 */
data class AuthUiState(val isLoading: Boolean, val error: Throwable? = null) : Cloneable<AuthUiState> {

    companion object {
        fun idle() = AuthUiState(false)
    }

    override fun clone(): AuthUiState = copy()
}