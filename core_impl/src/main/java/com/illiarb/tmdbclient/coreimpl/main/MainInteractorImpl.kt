package com.illiarb.tmdbclient.coreimpl.main

import com.illiarb.tmdblcient.core.modules.auth.Authenticator
import com.illiarb.tmdblcient.core.modules.main.MainInteractor
import com.illiarb.tmdblcient.core.navigation.AccountScreen
import com.illiarb.tmdblcient.core.navigation.AuthScreen
import com.illiarb.tmdblcient.core.navigation.MoviesScreen
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.navigation.ScreenName
import javax.inject.Inject

/**
 * @author ilya-rb on 20.11.18.
 */
class MainInteractorImpl @Inject constructor(
    private val router: Router,
    private val authenticator: Authenticator
) : MainInteractor {

    override fun onMainScreenSelected(screenName: ScreenName) {
        @Suppress("NON_EXHAUSTIVE_WHEN")
        when (screenName) {
            ScreenName.MOVIES -> router.navigateTo(MoviesScreen)
            ScreenName.ACCOUNT -> {
                if (authenticator.isAuthenticated()) {
                    router.navigateTo(AccountScreen)
                } else {
                    router.navigateTo(AuthScreen)
                }
            }
        }
    }
}