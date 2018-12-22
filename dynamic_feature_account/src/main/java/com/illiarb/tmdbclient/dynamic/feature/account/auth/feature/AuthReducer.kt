package com.illiarb.tmdbclient.dynamic.feature.account.auth.feature

import com.badoo.mvicore.element.Reducer
import com.illiarb.tmdbclient.dynamic.feature.account.auth.feature.AuthFeature.Effect
import javax.inject.Inject

/**
 * @author ilya-rb on 23.12.18.
 */
class AuthReducer @Inject constructor() : Reducer<AuthViewState, Effect> {

    override fun invoke(state: AuthViewState, effect: Effect): AuthViewState =
        when (effect) {
            is Effect.AuthenticationResult.InFlight -> state.copy(isLoading = true)
            is Effect.AuthenticationResult.Successful -> state.copy(isLoading = false)
            is Effect.AuthenticationResult.Failure -> state.copy(isLoading = false, error = effect.error)
            is Effect.CheckCredentialsResult.Valid -> state.copy(error = null)
            is Effect.CheckCredentialsResult.Invalid -> state.copy(error = effect.error)
        }
}