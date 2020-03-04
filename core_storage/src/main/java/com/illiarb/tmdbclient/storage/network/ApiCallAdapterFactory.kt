package com.illiarb.tmdbclient.storage.network

import com.illiarb.tmdbclient.storage.error.ErrorHandler
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.inject.Inject

class ApiCallAdapterFactory @Inject constructor(
  private val errorHandler: ErrorHandler
) : CallAdapter.Factory() {

  override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
    if (returnType !is ParameterizedType) {
      throw IllegalStateException(
        "Deferred return type must be parameterized as Deferred<Foo> or Deferred<out Foo>"
      )
    }

    return BodyCallAdapter<Any>(
      getParameterUpperBound(0, returnType),
      errorHandler
    )
  }

  private class BodyCallAdapter<T>(
    private val responseType: Type,
    private val errorHandler: ErrorHandler
  ) : CallAdapter<T, Deferred<T>> {

    override fun responseType() = responseType

    @Suppress("DeferredIsResult")
    override fun adapt(call: Call<T>): Deferred<T> {
      val deferred = CompletableDeferred<T>()

      deferred.invokeOnCompletion {
        if (!deferred.isCancelled) {
          call.cancel()
        }
      }

      call.enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
          deferred.completeExceptionally(errorHandler.createFromThrowable(t))
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
          if (response.isSuccessful) {
            deferred.complete(response.body()!!)
          } else {
            deferred.completeExceptionally(errorHandler.createFromErrorBody(response.errorBody()))
          }
        }
      })

      return deferred
    }
  }
}