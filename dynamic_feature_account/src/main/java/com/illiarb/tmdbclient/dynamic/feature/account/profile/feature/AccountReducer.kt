package com.illiarb.tmdbclient.dynamic.feature.account.profile.feature

import com.badoo.mvicore.element.Reducer
import com.illiarb.tmdbclient.dynamic.feature.account.profile.feature.AccountFeature.Effect
import javax.inject.Inject

/**
 * @author ilya-rb on 24.12.18.
 */
class AccountReducer @Inject constructor() : Reducer<AccountState, Effect> {

    override fun invoke(state: AccountState, effect: Effect): AccountState =
        when (effect) {
            is Effect.SignOut.InFlight -> state.copy(isBlockingLoading = false)
            is Effect.SignOut.Success -> state.copy(isLoading = false, isBlockingLoading = false)
            is Effect.SignOut.Failure -> state.copy(isBlockingLoading = false)
            is Effect.Account.InFlight -> state.copy(isLoading = false)
            is Effect.Account.Success -> state.copy(isLoading = false, account = effect.account)
            is Effect.Account.Failure -> state.copy(isLoading = false)
            is Effect.ShowMovieDetails -> state
        }
}