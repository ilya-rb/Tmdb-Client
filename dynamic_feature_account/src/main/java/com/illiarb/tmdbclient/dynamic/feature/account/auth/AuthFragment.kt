package com.illiarb.tmdbclient.dynamic.feature.account.auth

import android.os.Bundle
import android.view.View
import com.illiarb.tmdbclient.dynamic.feature.account.R
import com.illiarb.tmdbclient.dynamic.feature.account.di.AccountComponent
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.state.UiState
import com.illiarb.tmdbexplorerdi.Injectable
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import com.illiarb.tmdblcient.core.exception.ApiException
import com.illiarb.tmdblcient.core.exception.ErrorCodes
import com.illiarb.tmdblcient.core.exception.ValidationException
import com.illiarb.tmdblcient.core.ext.addTo
import kotlinx.android.synthetic.main.fragment_auth.*

/**
 * @author ilya-rb on 20.11.18.
 */
class AuthFragment : BaseFragment<AuthViewModel>(), Injectable {

    override fun getContentView(): Int = R.layout.fragment_auth

    override fun getViewModelClass(): Class<AuthViewModel> = AuthViewModel::class.java

    override fun inject(appProvider: AppProvider) = AccountComponent.get(appProvider).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAuthorize.setOnClickListener {
            val username = textUsername.text?.toString() ?: ""
            val password = textPassword.text?.toString() ?: ""
            viewModel.authorize(username, password)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.observeAuthState()
            .subscribe(::onAuthStateChanged, Throwable::printStackTrace)
            .addTo(destroyViewDisposable)
    }

    private fun onAuthStateChanged(state: UiState<Unit>) {
        if (state.isLoading()) {
            showProgressDialog()
        } else {
            hideProgressDialog()
        }

        when {
            state.hasError() -> {
                val error = state.requireError()
                when (error) {
                    is ValidationException -> showValidationErrors(error.errors)
                    is ApiException -> showError(error.message)
                }
            }
        }
    }

    private fun showValidationErrors(errors: List<Pair<Int, String>>) {
        errors.forEach { (code, message) ->
            when (code) {
                ErrorCodes.ERROR_USERNAME_EMPTY -> textUsername.error = message
                ErrorCodes.ERROR_PASSWORD_EMPTY -> textPassword.error = message
            }
        }
    }
}