package com.illiarb.tmdbclient.storage.network.error

import com.google.gson.JsonParser
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.Type
import javax.inject.Inject

class ApiCallAdapterFactory @Inject constructor(
    private val originalAdapterFactory: CallAdapter.Factory,
    private val jsonParser: JsonParser
) : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        @Suppress("UNCHECKED_CAST")
        val original = originalAdapterFactory.get(returnType, annotations, retrofit) as CallAdapter<Any, Any>
        return ApiCallAdapter(original, jsonParser)
    }
}