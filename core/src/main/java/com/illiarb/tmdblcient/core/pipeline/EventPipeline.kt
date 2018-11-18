package com.illiarb.tmdblcient.core.pipeline

import io.reactivex.Observable

/**
 * @author ilya-rb on 18.11.18.
 */
interface EventPipeline<T> {

    fun observeEvents(): Observable<T>

    fun dispatchEvent(data: T)
}