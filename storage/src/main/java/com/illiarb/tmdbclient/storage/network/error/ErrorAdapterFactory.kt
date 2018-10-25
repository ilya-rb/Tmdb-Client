package com.illiarb.tmdbclient.storage.network.error

import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.Type
import javax.inject.Inject

class ErrorAdapterFactory @Inject constructor(
    private val originalAdapterFactory: CallAdapter.Factory
) : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        @Suppress("UNCHECKED_CAST")
        val original = originalAdapterFactory.get(returnType, annotations, retrofit) as CallAdapter<Any, Any>
        return ErrorCallAdapter(original)
    }
}