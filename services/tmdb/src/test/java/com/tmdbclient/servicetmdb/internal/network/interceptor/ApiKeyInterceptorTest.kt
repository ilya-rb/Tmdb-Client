package com.tmdbclient.servicetmdb.internal.network.interceptor

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.tmdbclient.servicetmdb.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import org.junit.Assert.assertEquals
import org.junit.Test

class ApiKeyInterceptorTest {

  @Test
  fun `it should append build config api key as query parameter to request url`() {
    val interceptor = ApiKeyInterceptor()
    val chain = mock<Interceptor.Chain>()

    whenever(chain.request()).thenReturn(
      Request.Builder()
        .url("https://api-url.com/endpoint")
        .build()
    )

    interceptor.intercept(chain)

    val requestCaptor = argumentCaptor<Request>()
    verify(chain).proceed(requestCaptor.capture())

    val apiKeyParam = requestCaptor.firstValue.url.queryParameter(ApiKeyInterceptor.QUERY_PARAM_API_KEY)
    assertEquals(apiKeyParam, BuildConfig.API_KEY)
  }
}