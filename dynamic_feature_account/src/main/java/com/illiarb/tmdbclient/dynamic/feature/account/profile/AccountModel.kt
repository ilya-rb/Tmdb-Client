package com.illiarb.tmdbclient.dynamic.feature.account.profile

import com.illiarb.tmdbclient.dynamic.feature.account.profile.domain.GetProfileUseCase
import com.illiarb.tmdbclient.dynamic.feature.account.profile.domain.SignOutUseCase
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.navigation.AuthScreen
import com.illiarb.tmdblcient.core.navigation.Router
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author ilya-rb on 07.01.19.
 */
class AccountModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val router: Router
) : BasePresentationModel<AccountUiState>(), CoroutineScope {

    init {
        launch(context = coroutineContext) {
            val account = getProfileUseCase.execute(Unit)
            setState(AccountUiState(false, false, account))
        }
    }

    fun onLogoutClick() {
        launch(context = coroutineContext) {
            val success = signOutUseCase.execute(Unit)
            if (success) {
                router.navigateTo(AuthScreen)
            }
        }
    }
}