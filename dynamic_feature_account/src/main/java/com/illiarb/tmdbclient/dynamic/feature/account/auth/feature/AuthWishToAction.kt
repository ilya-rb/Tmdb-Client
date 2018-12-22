package com.illiarb.tmdbclient.dynamic.feature.account.auth.feature

import com.badoo.mvicore.element.WishToAction
import com.illiarb.tmdbclient.dynamic.feature.account.auth.feature.AuthFeature.Action
import com.illiarb.tmdbclient.dynamic.feature.account.auth.feature.AuthFeature.Wish
import javax.inject.Inject

/**
 * @author ilya-rb on 23.12.18.
 */
class AuthWishToAction @Inject constructor() : WishToAction<Wish, Action> {

    override fun invoke(wish: Wish): Action =
        when (wish) {
            is Wish.Authenticate -> Action.Authenticate(wish.username, wish.password)
            is Wish.ValidateCredentials -> Action.ValidateCredentials(wish.username, wish.password)
        }
}