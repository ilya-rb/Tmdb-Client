package com.illiarb.tmdbclient.tools

import com.illiarb.tmdblcient.core.system.EventBus
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * @author ilya-rb on 03.11.18.
 */
class RxEventBus @Inject constructor() : EventBus {

    private val busSubject = PublishSubject.create<Any>()

    override fun postEvent(data: Any) = busSubject.onNext(data)

    override fun <T> observeEvents(clazz: Class<T>): Observable<T> = busSubject.ofType(clazz).hide()
}