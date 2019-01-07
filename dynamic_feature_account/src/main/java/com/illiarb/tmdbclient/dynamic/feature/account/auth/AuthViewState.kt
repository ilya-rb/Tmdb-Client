package com.illiarb.tmdbclient.dynamic.feature.account.auth

/**
 * @author ilya-rb on 23.12.18.
 */
data class AuthViewState(val isLoading: Boolean, val error: Throwable? = null) {

    companion object {
        fun idle() = AuthViewState(false)
    }
}