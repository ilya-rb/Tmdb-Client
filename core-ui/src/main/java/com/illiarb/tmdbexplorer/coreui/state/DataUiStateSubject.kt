package com.illiarb.tmdbexplorer.coreui.state

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

abstract class DataUiStateSubject<Payload, Result> : UiStateSubject<Payload, Result> {

    /**
     * Subject that always keep current state of ui
     */
    private val dataSubject = BehaviorSubject.create<UiState<Result>>()

    /**
     * Creates some action that loads data
     */
    abstract fun createData(payload: Payload): Disposable

    override fun dispatchState(state: UiState<Result>) = dataSubject.onNext(state)

    override fun observer(): Observable<UiState<Result>> = dataSubject.hide()

    override fun loadData(payload: Payload) {
        createData(payload)
    }
}