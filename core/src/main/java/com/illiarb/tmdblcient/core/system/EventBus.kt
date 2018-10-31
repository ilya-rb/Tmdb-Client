package com.illiarb.tmdblcient.core.system

import io.reactivex.Observable

/**
 * @author ilya-rb on 30.10.18.
 */
interface EventBus {

    fun postEvent(data: Any)

    fun <T> observeEvents(clazz: Class<T>): Observable<T>
}