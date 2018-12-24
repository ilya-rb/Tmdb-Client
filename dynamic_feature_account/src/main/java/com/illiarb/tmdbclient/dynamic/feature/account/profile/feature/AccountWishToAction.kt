package com.illiarb.tmdbclient.dynamic.feature.account.profile.feature

import com.badoo.mvicore.element.WishToAction
import com.illiarb.tmdbclient.dynamic.feature.account.profile.feature.AccountFeature.Action
import com.illiarb.tmdbclient.dynamic.feature.account.profile.feature.AccountFeature.Wish
import javax.inject.Inject

/**
 * @author ilya-rb on 24.12.18.
 */
class AccountWishToAction @Inject constructor() : WishToAction<Wish, Action> {

    override fun invoke(wish: Wish): Action =
        when (wish) {
            is Wish.ShowAccount -> Action.ShowAccount
            is Wish.SignOut -> Action.SignOut
            is Wish.ShowMovieDetails -> Action.ShowMovieDetails(wish.id)
        }
}