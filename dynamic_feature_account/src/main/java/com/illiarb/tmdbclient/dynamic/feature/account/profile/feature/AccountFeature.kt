package com.illiarb.tmdbclient.dynamic.feature.account.profile.feature

import com.badoo.mvicore.feature.BaseFeature
import com.illiarb.tmdbclient.dynamic.feature.account.profile.feature.AccountFeature.*
import javax.inject.Inject

/**
 * @author ilya-rb on 24.12.18.
 */
class AccountFeature @Inject constructor(
    actor: AccountActor,
    newsPublisher: AccountNewsPublisher,
    reducer: AccountReducer,
    wishToAction: AccountWishToAction
) : BaseFeature<Wish, Action, Effect, AccountState, News>(
    initialState = AccountState.idle(),
    actor = actor,
    newsPublisher = newsPublisher,
    reducer = reducer,
    wishToAction = wishToAction
) {

    sealed class Wish {
        object ShowAccount : Wish()
        object SignOut : Wish()
        data class ShowMovieDetails(val id: Int) : Wish()
    }

    sealed class Action {
        object SignOut : Action()
        object ShowAccount : Action()
        data class ShowMovieDetails(val id: Int) : Action()
    }

    sealed class Effect {

        sealed class SignOut : Effect() {
            object InFlight : SignOut()
            object Success : SignOut()
            data class Failure(val error: Throwable) : SignOut()
        }

        sealed class Account : Effect() {
            object InFlight : Account()
            data class Success(val account: com.illiarb.tmdblcient.core.entity.Account) : Account()
            data class Failure(val error: Throwable) : Account()
        }

        data class ShowMovieDetails(val id: Int) : Effect()
    }

    sealed class News {
        object ShowAuthScreen : News()
        data class ShowMovieDetails(val id: Int) : News()
    }
}