package com.illiarb.tmdbclient.dynamic.feature.account.auth

import com.illiarb.tmdbclient.dynamic.feature.account.auth.domain.Authenticate
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.entity.UserCredentials
import com.illiarb.tmdblcient.core.navigation.AccountScreen
import com.illiarb.tmdblcient.core.navigation.Router
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author ilya-rb on 07.01.19.
 */
class AuthModel @Inject constructor(
    private val authenticate: Authenticate,
    private val router: Router
) : BasePresentationModel<AuthUiState>() {

    init {
        setIdleState(AuthUiState.idle())
    }

    fun onTextChanged(username: String? = null, password: String? = null) {
        setState {
            AuthUiState(it.isLoading, it.error, username != null && password != null)
        }
    }

    fun authenticate(username: String, password: String) = launch(context = coroutineContext) {
        setState { AuthUiState(true, it.error, false) }

        try {
            authenticate.execute(UserCredentials(username, password))
            router.navigateTo(AccountScreen)
        } catch (e: Exception) {
            setState { AuthUiState(false, e, true) }
        }
    }
}