package com.illiarb.tmdbexplorer.coreui.state

import io.reactivex.Observable
import io.reactivex.disposables.Disposable

interface UiStateSubject<Payload, Result> {

    fun observer(): Observable<UiState<Result>>

    fun loadData(payload: Payload)

    fun dispatchState(state: UiState<Result>)
}