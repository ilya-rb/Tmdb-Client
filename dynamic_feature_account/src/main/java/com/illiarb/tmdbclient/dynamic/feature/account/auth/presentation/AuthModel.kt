package com.illiarb.tmdbclient.dynamic.feature.account.auth.presentation

import com.illiarb.tmdbclient.dynamic.feature.account.auth.domain.Authenticate
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.domain.entity.UserCredentials
import com.illiarb.tmdblcient.core.navigation.AccountScreen
import com.illiarb.tmdblcient.core.navigation.Router
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author ilya-rb on 07.01.19.
 */
@ExperimentalCoroutinesApi
class AuthModel @Inject constructor(
    private val authenticate: Authenticate,
    private val router: Router
) : BasePresentationModel<AuthUiState>(AuthUiState.idle()) {

    fun onTextChanged(inputs: Array<String?>) {
        setState {
            copy(authButtonEnabled = inputs.all { input -> !input.isNullOrEmpty() })
        }
    }

    fun authenticate(username: String, password: String) = launch(context = coroutineContext) {
        setState { copy(isLoading = true, authButtonEnabled = false) }

        handleResult(
            authenticate.executeAsync(UserCredentials(username, password)),

            onSuccess = {
                setState {
                    copy(isLoading = false)
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