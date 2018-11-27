package com.illiarb.tmdbclient.dynamic.feature.account.auth

import com.illiarb.tmdbexplorer.coreui.state.DataUiStateSubject
import com.illiarb.tmdbexplorer.coreui.state.UiState
import com.illiarb.tmdbexplorer.coreui.state.subscribe
import com.illiarb.tmdbexplorer.coreui.viewmodel.BaseViewModel
import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.ext.ioToMain
import com.illiarb.tmdblcient.core.modules.auth.AuthInteractor
import com.illiarb.tmdblcient.core.system.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * @author ilya-rb on 20.11.18.
 */
class AuthViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private val authStateSubject = object : DataUiStateSubject<AuthPayload, Unit>() {
        override fun createData(payload: AuthPayload): Disposable =
            authInteractor.authenticate(payload.username, payload.password)
                .ioToMain(schedulerProvider)
                .subscribe(this)
                .addTo(clearDisposable)
    }

    fun observeAuthState(): Observable<UiState<Unit>> = authStateSubject.observer()

    fun authorize(username: String, password: String) = authStateSubject.loadData(AuthPayload(username, password))

    private class AuthPayload(val username: String, val password: String)
}