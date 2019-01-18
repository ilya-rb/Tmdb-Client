package com.illiarb.tmdbclient.dynamic.feature.account.auth.presentation

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

    fun onTextChanged(inputs: Array<String?>) {
        setState {
            it.copy(authButtonEnabled = inputs.all { input -> !input.isNullOrEmpty() })
        }
    }

    fun authenticate(username: String, password: String) = launch(context = coroutineContext) {
        setState { it.copy(isLoading = true, authButtonEnabled = false) }

        handleResult(
            authenticate.executeAsync(UserCredentials(username, password)),

            onSuccess = {
                setState { current ->
                    current.copy(isLoading = false)
                }
                router.navigateTo(AccountScreen)
            },

            onError = { throwable ->
                super.handleError(throwable)

                setState {
                    AuthUiState(false, throwable, true)
                }
            }
        )
    }
}