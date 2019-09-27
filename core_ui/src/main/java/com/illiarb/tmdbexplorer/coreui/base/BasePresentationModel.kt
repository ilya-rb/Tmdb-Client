package com.illiarb.tmdbexplorer.coreui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

/**
 * @author ilya-rb on 07.01.19.
 */
abstract class BasePresentationModel : ViewModel() {

    protected val scope: CoroutineScope
        get() = viewModelScope
}