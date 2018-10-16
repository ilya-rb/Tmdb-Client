package com.illiarb.tmdbexplorer.coreui.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    protected val clearDisposable = CompositeDisposable()

    override fun onCleared() {
        clearDisposable.clear()
    }

}