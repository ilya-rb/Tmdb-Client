package com.illiarb.tmdbclient.dynamic.feature.account.auth.ui

import android.os.Bundle
import android.text.Editable
import android.view.View
import com.badoo.mvicore.binder.Binder
import com.illiarb.tmdbclient.dynamic.feature.account.R
import com.illiarb.tmdbclient.dynamic.feature.account.auth.feature.AuthFeature
import com.illiarb.tmdbclient.dynamic.feature.account.auth.feature.AuthFeature.Wish.Authenticate
import com.illiarb.tmdbclient.dynamic.feature.account.auth.feature.AuthFeature.Wish.ValidateCredentials
import com.illiarb.tmdbclient.dynamic.feature.account.auth.feature.AuthViewState
import com.illiarb.tmdbclient.dynamic.feature.account.di.AccountComponent
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.exception.ApiException
import com.illiarb.tmdblcient.core.exception.ErrorCodes
import com.illiarb.tmdblcient.core.exception.ValidationException
import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.navigation.AccountScreen
import com.illiarb.tmdblcient.core.navigation.Router
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_auth.*
import javax.inject.Inject

/**
 * @author ilya-rb on 20.11.18.
 */
class AuthFragment : BaseFragment(), Injectable, Consumer<AuthViewState> {

    @Inject
    lateinit var feature: AuthFeature

    @Inject
    lateinit var router: Router

    private val usernamePublisher = PublishSubject.create<String>()
    private val passwordPublisher = PublishSubject.create<String>()

    private val binder = Binder()

    private val newsListener = Consumer<AuthFeature.News> {
        when (it) {
            AuthFeature.News.NavigateToAccount -> router.navigateTo(AccountScreen)
        }
    }

    override fun getContentView(): Int = R.layout.fragment_auth

    override fun inject(appProvider: AppProvider) = AccountComponent.get(appProvider).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi(view)
        setupBindings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binder.clear()
    }

    override fun accept(state: AuthViewState) {
        if (state.error != null) {
            when (state.error) {
                is ValidationException -> showValidationErrors(state.error.errors)
                is ApiException -> showError(state.error.message)
            }
        } else {
            textUsername.error = null
            textPassword.error = null
        }

        btnAuthorize.isEnabled = state.error == null || state.error !is ValidationException
    }

    private fun setupUi(@Suppress("UNUSED_PARAMETER") view: View) {
        textUsername.addTextChangedListener(object : TextWatcherAdapter() {
            override fun afterTextChanged(editable: Editable?) {
                editable?.let {
                    usernamePublisher.onNext(it.toString())
                }
            }
        })

        textPassword.addTextChangedListener(object : TextWatcherAdapter() {
            override fun afterTextChanged(editable: Editable?) {
                editable?.let {
                    passwordPublisher.onNext(it.toString())
                }
            }
        })

        Observable
            .combineLatest(
                usernamePublisher,
                passwordPublisher,
                BiFunction { username: String, password: String -> username to password }
            )
            .subscribe({ (username, password) -> feature.accept(ValidateCredentials(username, password)) }, Throwable::printStackTrace)
            .addTo(destroyViewDisposable)

        btnAuthorize.setOnClickListener {
            val username = textUsername.text?.toString() ?: ""
            val password = textPassword.text?.toString() ?: ""
            feature.accept(Authenticate(username, password))
        }
    }

    private fun setupBindings() {
        binder.bind(feature to this)
        binder.bind(feature.news to newsListener)
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