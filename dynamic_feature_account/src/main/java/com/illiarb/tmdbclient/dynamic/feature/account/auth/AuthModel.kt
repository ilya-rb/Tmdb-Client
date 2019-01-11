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

    fun onTextChanged(inputs: Array<String>) {
        setState { current ->
            AuthUiState(current.isLoading, current.error, inputs.all { it.isNotEmpty() })
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