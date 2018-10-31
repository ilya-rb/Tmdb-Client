package com.illiarb.tmdbclient.system

import com.illiarb.tmdblcient.core.system.EventBus
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author ilya-rb on 30.10.18.
 */
class AndroidEventBus @Inject constructor() : EventBus {

    private val busSubject = PublishSubject.create<Any>()

    override fun postEvent(data: Any) = busSubject.onNext(data)

    override fun <T> observeEvents(clazz: Class<T>): Observable<T> = busSubject.ofType(clazz).hide()
}