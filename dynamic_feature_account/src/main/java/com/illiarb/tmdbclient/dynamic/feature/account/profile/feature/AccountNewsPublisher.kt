package com.illiarb.tmdbclient.dynamic.feature.account.profile.feature

import com.badoo.mvicore.element.NewsPublisher
import com.illiarb.tmdbclient.dynamic.feature.account.profile.feature.AccountFeature.*
import javax.inject.Inject

/**
 * @author ilya-rb on 24.12.18.
 */
class AccountNewsPublisher @Inject constructor() : NewsPublisher<Action, Effect, AccountState, News> {

    override fun invoke(action: Action, effect: Effect, p3: AccountState): News? =
        when (effect) {
            is Effect.SignOut.Success -> News.ShowAuthScreen
            is Effect.ShowMovieDetails -> News.ShowMovieDetails(effect.id)
            else -> null
        }
}