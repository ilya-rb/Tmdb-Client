package com.illiarb.tmdbclient.coreimpl.main

import com.illiarb.tmdblcient.core.modules.main.MainInteractor
import com.illiarb.tmdblcient.core.navigation.AccountScreenData
import com.illiarb.tmdblcient.core.navigation.AuthScreenData
import com.illiarb.tmdblcient.core.navigation.ExploreScreenData
import com.illiarb.tmdblcient.core.navigation.MoviesScreenData
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.navigation.ScreenName
import com.illiarb.tmdblcient.core.storage.AccountRepository
import javax.inject.Inject

/**
 * @author ilya-rb on 20.11.18.
 */
class MainInteractorImpl @Inject constructor(
    private val router: Router,
    private val accountRepository: AccountRepository
) : MainInteractor {

    override fun onMainScreenSelected(screenName: ScreenName) {
        @Suppress("NON_EXHAUSTIVE_WHEN")
        when (screenName) {
            ScreenName.MOVIES -> router.navigateTo(MoviesScreenData)
            ScreenName.EXPLORE -> router.navigateTo(ExploreScreenData)
            ScreenName.ACCOUNT -> {
                if (accountRepository.isAuthorized()) {
                    router.navigateTo(AccountScreenData)
                } else {
                    router.navigateTo(AuthScreenData)
                }
            }
        }
    }
}