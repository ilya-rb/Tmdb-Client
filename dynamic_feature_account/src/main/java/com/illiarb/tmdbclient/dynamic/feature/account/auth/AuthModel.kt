package com.illiarb.tmdbclient.dynamic.feature.account.auth

import com.illiarb.tmdbexplorer.coreui.base.BaseViewModel
import com.illiarb.tmdblcient.core.domain.auth.Authenticate
import com.illiarb.tmdblcient.core.domain.auth.ValidateCredentials
import com.illiarb.tmdblcient.core.system.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * @author ilya-rb on 07.01.19.
 */
class AuthModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val authenticate: Authenticate,
    private val validateCredentials: ValidateCredentials
) : BaseViewModel() {

    override fun provideDefaultDispatcher(): CoroutineDispatcher = dispatcherProvider.mainDispatcher
}