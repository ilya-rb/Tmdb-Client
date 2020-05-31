package com.illiarb.tmdbclient.services.tmdb.internal.network

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.internal.error.ErrorCreator
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
  private val errorCreator: ErrorCreator
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
            ResultAdapter(resultType, errorCreator)
          }
          else -> null
        }
      }
      else -> null
    }
}

class ResultAdapter(
  private val type: Type,
  private val errorCreator: ErrorCreator
) : CallAdapter<Type, Call<Result<Type>>> {
  override fun responseType() = type
  override fun adapt(call: Call<Type>): Call<Result<Type>> =
    ResultCall(call, errorCreator)
}

class ResultCall<T>(
  proxy: Call<T>,
  private val errorCreator: ErrorCreator
) : CallDelegate<T, Result<T>>(proxy) {

  override fun timeout(): Timeout = Timeout.NONE

  override fun cloneImpl() = ResultCall(proxy.clone(), errorCreator)

  override fun enqueueImpl(callback: Callback<Result<T>>) = proxy.enqueue(object : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
      val result: Result<T> = if (response.isSuccessful) {
        @Suppress("USELESS_CAST")
        // false positive
        Result.Ok(response.body()!!) as Result<T>
      } else {
        Result.Err(errorCreator.createFromErrorBody(response.errorBody()))
      }
      callback.onResponse(this@ResultCall, Response.success(result))
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
      callback.onResponse(
        this@ResultCall,
        Response.success(Result.Err(errorCreator.createFromThrowable(t)))
      )
    }
  })
}