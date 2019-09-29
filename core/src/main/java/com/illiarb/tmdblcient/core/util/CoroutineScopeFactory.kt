package com.illiarb.tmdblcient.core.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object CoroutineScopeFactory {

    private var customViewModelScope: CoroutineScope? = null

    val newViewModelScope: CoroutineScope
        get() = customViewModelScope ?: CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun setCustomViewModelScope(scope: CoroutineScope) {
        customViewModelScope = scope
    }

    fun resetCustomViewModelScope() {
        customViewModelScope = null
    }

}