package com.illiarb.tmdbexplorer.coreui.base

import androidx.lifecycle.ViewModel
import com.illiarb.tmdblcient.core.util.CoroutineScopeFactory
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * @author ilya-rb on 07.01.19.
 */
abstract class BasePresentationModel : ViewModel() {

    private val scope = CoroutineScopeFactory.newViewModelScope

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

    protected fun launch(block: suspend () -> Unit) {
        scope.launch {
            block()
        }
    }
}