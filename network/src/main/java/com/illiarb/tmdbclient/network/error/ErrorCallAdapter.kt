package com.illiarb.tmdbclient.network.error

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ErrorCallAdapter(private val originalAdapter: CallAdapter<Any, Any>) : CallAdapter<Any, Any> {

    override fun adapt(call: Call<Any>): Any {
        val result = originalAdapter.adapt(call)

        return when (result) {
            is Observable<*> -> result.onErrorResumeNext(Function { Observable.error(processError(it)) })
            is Single<*> -> result.onErrorResumeNext(Function { Single.error(processError(it)) })
            is Flowable<*> -> result.onErrorResumeNext(Function { Flowable.error(processError(it)) })
            is Maybe<*> -> result.onErrorResumeNext(Function { Maybe.error(processError(it)) })
            is Completable -> result.onErrorResumeNext(Function { Completable.error(processError(it)) })

            else -> result
        }
    }

    override fun responseType(): Type = originalAdapter.responseType()

    private fun processError(throwable: Throwable): Throwable {
        return throwable
    }
}