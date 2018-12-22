package com.illiarb.tmdbclient.dynamic.feature.account.auth.feature

import com.badoo.mvicore.element.NewsPublisher
import com.illiarb.tmdbclient.dynamic.feature.account.auth.feature.AuthFeature.*
import javax.inject.Inject

/**
 * @author ilya-rb on 23.12.18.
 */
class AuthNewsPublisher @Inject constructor() : NewsPublisher<Action, Effect, AuthViewState, News> {

    override fun invoke(action: Action, effect: Effect, state: AuthViewState): News? =
        when (effect) {
            Effect.AuthenticationResult.Successful -> News.NavigateToAccount
            else -> null
        }
}