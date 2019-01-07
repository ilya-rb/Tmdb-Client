package com.illiarb.tmdbclient.dynamic.feature.account.profile

import androidx.lifecycle.ViewModel
import com.illiarb.tmdblcient.core.domain.profile.GetProfileUseCase
import com.illiarb.tmdblcient.core.entity.Account
import com.illiarb.tmdblcient.core.system.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * @author ilya-rb on 07.01.19.
 */
class AccountModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()

    private val stateHolder = StateHolder()

    override val coroutineContext: CoroutineContext = job + dispatcherProvider.mainDispatcher

    init {
        launch(context = coroutineContext) {
            val account = getProfileUseCase()
            stateHolder.setValue(ProfileState(false, account))
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    class StateHolder {

        private var _state: ProfileState? = null

        val state: ProfileState
            get() = _state ?: ProfileState(false, null)

        fun setValue(newState: ProfileState) {
            this._state = ProfileState(newState.isLoading, newState.account)
        }

        fun getValue(): ProfileState = state.copy()
    }

    data class ProfileState(val isLoading: Boolean, val account: Account? = null)
}