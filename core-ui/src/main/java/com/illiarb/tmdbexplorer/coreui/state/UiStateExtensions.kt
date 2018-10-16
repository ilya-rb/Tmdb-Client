package com.illiarb.tmdbexplorer.coreui.state

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable

fun Completable.subscribe(stateSubject: UiStateSubject<*, Unit>): Disposable {
    return this
        .doOnSubscribe { stateSubject.dispatchState(UiState.createLoadingState()) }
        .subscribe(
            { stateSubject.dispatchState(UiState.createSuccessState(Unit)) },
            { stateSubject.dispatchState(UiState.createErrorState(it)) }
        )
}

fun <T> Single<T>.subscribe(stateSubject: UiStateSubject<*, T>): Disposable {
    return this
        .doOnSubscribe { stateSubject.dispatchState(UiState.createLoadingState()) }
        .subscribe(
            { stateSubject.dispatchState(UiState.createSuccessState(it)) },
            { stateSubject.dispatchState(UiState.createErrorState(it)) }
        )
}

fun <T> Maybe<T>.subscribe(stateSubject: UiStateSubject<*, T>): Disposable {
    return this
        .doOnSubscribe { stateSubject.dispatchState(UiState.createLoadingState()) }
        .subscribe(
            { stateSubject.dispatchState(UiState.createSuccessState(it)) },
            { stateSubject.dispatchState(UiState.createErrorState(it)) }
        )
}

fun <T> Observable<T>.subscribe(stateSubject: UiStateSubject<*, T>): Disposable {
    return this
        .doOnSubscribe { stateSubject.dispatchState(UiState.createLoadingState()) }
        .subscribe(
            { stateSubject.dispatchState(UiState.createSuccessState(it)) },
            { stateSubject.dispatchState(UiState.createErrorState(it)) }
        )
}