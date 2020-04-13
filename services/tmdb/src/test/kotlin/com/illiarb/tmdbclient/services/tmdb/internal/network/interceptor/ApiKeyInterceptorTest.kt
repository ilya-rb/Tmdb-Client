package com.illiarb.tmdbclient.services.tmdb.internal.network.interceptor

import com.illiarb.tmdbclient.services.tmdb.BuildConfig
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Test

class ApiKeyInterceptorTest {

  @Test
  fun `it should append build config api key as query parameter to request url`() {
    val interceptor = ApiKeyInterceptor()
    val chain = mock<Interceptor.Chain>()
    val request = Request.Builder()
      .url("https://api-url.com/endpoint")
      .build()

    whenever(chain.request()).thenReturn(request)
    whenever(chain.proceed(any())).thenReturn(
      Response.Builder()
        .request(request)
        .protocol(Protocol.HTTP_1_1)
        .message("")
        .code(200)
        .build()
    )

    interceptor.intercept(chain)

    val requestCaptor = argumentCaptor<Request>()
    verify(chain).proceed(requestCaptor.capture())

    val apiKeyParam = requestCaptor.firstValue.url.queryParameter(ApiKeyInterceptor.QUERY_PARAM_API_KEY)
    assertEquals(apiKeyParam, BuildConfig.API_KEY)
  }
}