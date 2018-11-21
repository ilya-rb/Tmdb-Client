package com.illiarb.tmdbclient.coreimpl.main

import com.illiarb.tmdblcient.core.modules.auth.Authenticator
import com.illiarb.tmdblcient.core.modules.main.MainInteractor
import com.illiarb.tmdblcient.core.navigation.AccountScreenData
import com.illiarb.tmdblcient.core.navigation.AuthScreenData
import com.illiarb.tmdblcient.core.navigation.ExploreScreenData
import com.illiarb.tmdblcient.core.navigation.MoviesScreenData
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
            ScreenName.MOVIES -> router.navigateTo(MoviesScreenData)
            ScreenName.EXPLORE -> router.navigateTo(ExploreScreenData)
            ScreenName.ACCOUNT -> {
                if (authenticator.isAuthenticated()) {
                    router.navigateTo(AccountScreenData)
                } else {
                    router.navigateTo(AuthScreenData)
                }
            }
        }
    }
}