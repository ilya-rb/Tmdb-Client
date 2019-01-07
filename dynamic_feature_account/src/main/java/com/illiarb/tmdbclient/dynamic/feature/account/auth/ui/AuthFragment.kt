package com.illiarb.tmdbclient.dynamic.feature.account.auth.ui

import android.os.Bundle
import android.view.View
import androidx.transition.Fade
import com.illiarb.tmdbclient.dynamic.feature.account.R
import com.illiarb.tmdbclient.dynamic.feature.account.auth.AuthModel
import com.illiarb.tmdbclient.dynamic.feature.account.auth.AuthViewState
import com.illiarb.tmdbclient.dynamic.feature.account.di.AccountComponent
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.exception.ApiException
import com.illiarb.tmdblcient.core.exception.ErrorCodes
import com.illiarb.tmdblcient.core.exception.ValidationException
import kotlinx.android.synthetic.main.fragment_auth.*
import javax.inject.Inject

/**
 * @author ilya-rb on 20.11.18.
 */
class AuthFragment : BaseFragment(), Injectable {

    @Inject
    lateinit var authModel: AuthModel

    override fun getContentView(): Int = R.layout.fragment_auth

    override fun inject(appProvider: AppProvider) = AccountComponent.get(appProvider).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = Fade()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi(view)
    }

    private fun render(state: AuthViewState) {
        if (state.error != null) {
            when (state.error) {
                is ValidationException -> showValidationErrors(state.error.errors)
                is ApiException -> showErrorDialog(state.error.message)
            }
        } else {
            textUsername.error = null
            textPassword.error = null
        }

        btnAuthorize.isEnabled = state.error == null || state.error !is ValidationException
    }

    private fun setupUi(@Suppress("UNUSED_PARAMETER") view: View) {
        textUsername.addTextChangedListener(object : TextWatcherAdapter() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
            }
        })

        textPassword.addTextChangedListener(object : TextWatcherAdapter() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
            }
        })

//        Observable
//            .combineLatest(
//                usernamePublisher,
//                passwordPublisher,
//                BiFunction { username: String, password: String -> username to password }
//            )
//            .subscribe({ (username, password) -> feature.accept(ValidateCredentials(username, password)) }, Throwable::printStackTrace)
//            .addTo(destroyViewDisposable)

        btnAuthorize.setOnClickListener {
            val username = textUsername.text?.toString() ?: ""
            val password = textPassword.text?.toString() ?: ""
//            feature.accept(Authenticate(username, password))
        }
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
}