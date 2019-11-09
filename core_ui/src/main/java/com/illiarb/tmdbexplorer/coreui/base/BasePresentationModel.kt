package com.illiarb.tmdbexplorer.coreui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * @author ilya-rb on 07.01.19.
 */
abstract class BasePresentationModel : ViewModel() {

    private val scope = viewModelScope

    protected fun launch(block: suspend () -> Unit) {
        scope.launch { block() }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}