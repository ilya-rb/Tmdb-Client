package com.illiarb.tmdbclient.dynamic.feature.account

import com.illiarb.tmdbexplorer.coreui.state.DataUiStateSubject
import com.illiarb.tmdbexplorer.coreui.state.subscribe
import com.illiarb.tmdbexplorer.coreui.viewmodel.BaseViewModel
import com.illiarb.tmdblcient.core.entity.Account
import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.ext.ioToMain
import com.illiarb.tmdblcient.core.modules.account.AccountInteractor
import com.illiarb.tmdblcient.core.system.SchedulerProvider
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * @author ilya-rb on 20.11.18.
 */
class AccountViewModel @Inject constructor(
    private val accountInteractor: AccountInteractor,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private val accountState = object : DataUiStateSubject<Unit, Account>() {
        override fun createData(payload: Unit): Disposable =
            accountInteractor.getCurrentAccount()
                .ioToMain(schedulerProvider)
                .subscribe(this)
                .addTo(clearDisposable)
    }

    init {
        accountState.loadData(Unit)
    }

    fun observeAccountState() = accountState.observer()

    fun onLogoutClicked() {
        accountInteractor.onLogoutClicked()
    }
}