package com.illiarb.tmdbexplorer.coreui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * @author ilya-rb on 07.01.19.
 */
abstract class BasePresentationModel : ViewModel() {

    protected fun launch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(block = block)
    }
}