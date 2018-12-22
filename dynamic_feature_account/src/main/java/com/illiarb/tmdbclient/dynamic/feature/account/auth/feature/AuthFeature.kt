package com.illiarb.tmdbclient.dynamic.feature.account.auth.feature

import com.badoo.mvicore.feature.BaseFeature
import com.illiarb.tmdbclient.dynamic.feature.account.auth.feature.AuthFeature.*
import com.illiarb.tmdbclient.dynamic.feature.account.auth.feature.actor.AuthActor
import javax.inject.Inject

/**
 * @author ilya-rb on 23.12.18.
 */
class AuthFeature @Inject constructor(
    actor: AuthActor,
    wishToAction: AuthWishToAction,
    reducer: AuthReducer,
    newsPublisher: AuthNewsPublisher
) : BaseFeature<Wish, Action, Effect, AuthViewState, News>(
    initialState = AuthViewState.idle(),
    actor = actor,
    wishToAction = wishToAction,
    reducer = reducer,
    newsPublisher = newsPublisher
) {

    sealed class Wish {
        data class Authenticate(val username: String, val password: String) : Wish()
        data class ValidateCredentials(val username: String, val password: String) : Wish()
    }

    sealed class Action {
        data class Authenticate(val username: String, val password: String) : Action()
        data class ValidateCredentials(val username: String, val password: String) : Action()
    }

    sealed class Effect {

        sealed class AuthenticationResult : Effect() {
            object InFlight : AuthenticationResult()
            object Successful : AuthenticationResult()
            data class Failure(val error: Throwable) : AuthenticationResult()
        }

        sealed class CheckCredentialsResult : Effect() {
            object Valid : CheckCredentialsResult()
            data class Invalid(val error: Throwable) : CheckCredentialsResult()
        }
    }

    sealed class News {
        object NavigateToAccount : News()
    }
}