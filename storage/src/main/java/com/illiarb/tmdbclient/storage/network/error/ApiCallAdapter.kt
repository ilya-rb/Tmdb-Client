package com.illiarb.tmdbclient.storage.network.error

import com.google.gson.JsonParser
import com.illiarb.tmdblcient.core.exception.ApiException
import com.illiarb.tmdblcient.core.system.Logger
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import java.lang.reflect.Type

class ApiCallAdapter(
    private val originalAdapter: CallAdapter<Any, Any>,
    private val jsonParser: JsonParser
) : CallAdapter<Any, Any> {

    companion object {
        const val FIELD_STATUS_MESSAGE = "status_message"
        const val FIELD_STATUS_CODE = "status_code"
    }

    override fun adapt(call: Call<Any>): Any =
        when (val result = originalAdapter.adapt(call)) {
            is Observable<*> -> result.onErrorResumeNext(Function { Observable.error(processError(it)) })
            is Single<*> -> result.onErrorResumeNext(Function { Single.error(processError(it)) })
            is Flowable<*> -> result.onErrorResumeNext(Function { Flowable.error(processError(it)) })
            is Maybe<*> -> result.onErrorResumeNext(Function { Maybe.error(processError(it)) })
            is Completable -> result.onErrorResumeNext(Function { Completable.error(processError(it)) })
            else -> result
        }

    override fun responseType(): Type = originalAdapter.responseType()

    private fun processError(throwable: Throwable): Throwable {
        Logger.e("Api call error", throwable)

        if (throwable !is HttpException) {
            return throwable
        }

        val errorResponse = throwable.response().errorBody()
        if (errorResponse != null) {
            val rootElement = jsonParser.parse(errorResponse.string()).asJsonObject

            val statusCode = rootElement.get(FIELD_STATUS_CODE)
            val statusMessage = rootElement.get(FIELD_STATUS_MESSAGE)

            if (statusCode != null && statusMessage != null) {
                return ApiException(statusCode.asInt, statusMessage.asString)
            }
        }

        return throwable
    }
}