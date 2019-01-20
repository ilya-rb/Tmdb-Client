package com.illiarb.tmdbclient.dynamic.feature.account.auth.ui

import android.os.Bundle
import android.view.View
import androidx.transition.Fade
import com.illiarb.tmdbclient.dynamic.feature.account.R
import com.illiarb.tmdbclient.dynamic.feature.account.auth.presentation.AuthModel
import com.illiarb.tmdbclient.dynamic.feature.account.auth.presentation.AuthUiState
import com.illiarb.tmdbclient.dynamic.feature.account.di.AccountComponent
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.util.LawObserver
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.exception.ErrorCodes
import com.illiarb.tmdblcient.core.exception.ValidationException
import kotlinx.android.synthetic.main.fragment_auth.*
import kotlin.LazyThreadSafetyMode.NONE

/**
 * @author ilya-rb on 20.11.18.
 */
class AuthFragment : BaseFragment<AuthModel>(), Injectable {

    private val stateObserver: LawObserver<AuthUiState> by lazy(NONE) {
        object : LawObserver<AuthUiState>(presentationModel.stateObservable()) {
            override fun onNewValue(value: AuthUiState) {
                render(value)
            }
        }
    }

    override fun getContentView(): Int = R.layout.fragment_auth

    override fun inject(appProvider: AppProvider) = AccountComponent.get(appProvider).inject(this)

    override fun getModelClass(): Class<AuthModel> = AuthModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = Fade()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textUsername.addTextChangedListener(object : TextWatcherAdapter() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                presentationModel.onTextChanged(getInputValues())
            }
        })

        textPassword.addTextChangedListener(object : TextWatcherAdapter() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                presentationModel.onTextChanged(getInputValues())
            }
        })

        btnAuthorize.setOnClickListener {
            val username = textUsername.text?.toString() ?: ""
            val password = textPassword.text?.toString() ?: ""
            presentationModel.authenticate(username, password)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        stateObserver.register(this)
    }

    private fun render(state: AuthUiState) {
        if (state.error != null) {
            when (state.error) {
                is ValidationException -> showValidationErrors(state.error.errors)
            }
        }
        btnAuthorize.isEnabled = state.authButtonEnabled
    }

    private fun showValidationErrors(errors: List<Pair<Int, String>>) {
        errors.forEach { (code, message) ->
            when (code) {
                ErrorCodes.ERROR_USERNAME_EMPTY -> textUsername.error = message
                ErrorCodes.ERROR_PASSWORD_EMPTY,
                ErrorCodes.ERROR_PASSWORD_LENGTH -> textPassword.error = message
            }
        }
    }

    private fun getInputValues(): Array<String?> = arrayOf(
        textUsername.text?.toString(),
        textPassword.text?.toString()
    )
}