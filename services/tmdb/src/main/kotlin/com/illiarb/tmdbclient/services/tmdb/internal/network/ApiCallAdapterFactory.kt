package com.illiarb.tmdbclient.services.tmdb.internal.network

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.internal.error.ErrorHandler
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.inject.Inject

class CallAdapterFactory @Inject constructor(
  private val errorHandler: ErrorHandler
) : CallAdapter.Factory() {

  override fun get(
    returnType: Type,
    annotations: Array<Annotation>,
    retrofit: Retrofit
  ): CallAdapter<*, *>? =
    when (getRawType(returnType)) {
      Call::class.java -> {
        val callType = getParameterUpperBound(0, returnType as ParameterizedType)
        when (getRawType(callType)) {
          Result::class.java -> {
            val resultType = getParameterUpperBound(0, callType as ParameterizedType)
            ResultAdapter(resultType, errorHandler)
          }
          else -> null
        }
      }
      else -> null
    }
}

class ResultAdapter(
  private val type: Type,
  private val errorHandler: ErrorHandler
) : CallAdapter<Type, Call<Result<Type>>> {
  override fun responseType() = type
  override fun adapt(call: Call<Type>): Call<Result<Type>> =
    ResultCall(call, errorHandler)
}

class ResultCall<T>(
  proxy: Call<T>,
  private val errorHandler: ErrorHandler
) : CallDelegate<T, Result<T>>(proxy) {

  override fun timeout(): Timeout = Timeout.NONE

  override fun cloneImpl() = ResultCall(proxy.clone(), errorHandler)

  override fun enqueueImpl(callback: Callback<Result<T>>) = proxy.enqueue(object : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
      val result: Result<T> = if (response.isSuccessful) {
        Result.Ok(response.body()!!) as Result<T>
      } else {
        Result.Err(
          errorHandler.createFromErrorBody(
            response.errorBody()
          )
        )
      }
      callback.onResponse(this@ResultCall, Response.success(result))
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
      callback.onResponse(
        this@ResultCall,
        Response.success(Result.Err(errorHandler.createFromThrowable(t)))
      )
    }
  })
}