package com.illiarb.tmdblcient.core.pipeline

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * @author ilya-rb on 18.11.18.
 */
class SimpleEventPipeline<T> : EventPipeline<T> {

    private val pipelineSubject = PublishSubject.create<T>()

    override fun observeEvents(): Observable<T> = pipelineSubject.hide()

    override fun dispatchEvent(data: T) = pipelineSubject.onNext(data)
}