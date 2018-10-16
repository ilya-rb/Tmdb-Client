package com.illiarb.tmdblcient.core.ext

import com.illiarb.tmdblcient.core.schedulers.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.addTo(compositeDisposable: CompositeDisposable): Disposable {
    compositeDisposable.add(this)
    return this
}

fun <T> Single<T>.ioToMain(schedulersProvider: SchedulerProvider): Single<T> {
    return this
        .subscribeOn(schedulersProvider.provideIoScheduler())
        .observeOn(schedulersProvider.provideMainThreadScheduler())
}

fun <T> Observable<T>.ioToMain(schedulersProvider: SchedulerProvider): Observable<T> {
    return this
        .subscribeOn(schedulersProvider.provideIoScheduler())
        .observeOn(schedulersProvider.provideMainThreadScheduler())
}

fun <T> Maybe<T>.ioToMain(schedulersProvider: SchedulerProvider): Maybe<T> {
    return this
        .subscribeOn(schedulersProvider.provideIoScheduler())
        .observeOn(schedulersProvider.provideMainThreadScheduler())
}

fun Completable.ioToMain(schedulersProvider: SchedulerProvider): Completable {
    return this
        .subscribeOn(schedulersProvider.provideIoScheduler())
        .observeOn(schedulersProvider.provideMainThreadScheduler())
}