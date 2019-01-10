package com.illiarb.tmdbclient.dynamic.feature.account.auth

import com.illiarb.tmdbclient.dynamic.feature.account.auth.domain.Authenticate
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author ilya-rb on 07.01.19.
 */
class AuthModel @Inject constructor(
    private val authenticate: Authenticate
) : BasePresentationModel<AuthUiState>() {

    fun authenticate(username: String, password: String) = launch(context = coroutineContext) {

    }
}