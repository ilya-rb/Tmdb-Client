package com.illiarb.tmdbclient.services.tmdb.internal.network.interceptor

import com.google.common.truth.Truth.assertThat
import com.illiarb.tmdbclient.libs.tools.ResourceResolver
import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.domain.Country
import com.illiarb.tmdbclient.services.tmdb.internal.repository.ConfigurationRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.jupiter.api.Test
import java.util.Locale

class RegionInterceptorTest {

  @Test
  fun `it should append user region as query parameter`() = runBlockingTest {
    val region = "UA"
    val interceptor = createInterceptorWithRegion(region)
    val request = Request.Builder()
      .url("https://api-url.com/request")
      .build()

    val chain = mock<Interceptor.Chain>().also {
      whenever(it.request()).thenReturn(request)
      whenever(it.proceed(any())).thenReturn(
        Response.Builder()
          .request(request)
          .protocol(Protocol.HTTP_1_1)
          .message("")
          .code(200)
          .build()
      )
    }

    val intercepted = interceptor.captureInterceptedRequest(chain)
    assertThat(region).isEqualTo(intercepted.url.queryParameter("region"))
  }

  private suspend fun createInterceptorWithRegion(region: String): RegionInterceptor {
    val repository = mock<ConfigurationRepository>().also {
      whenever(it.getCountries()).thenReturn(
        Result.Ok(listOf(Country(region, region)))
      )
    }

    val resolver = mock<ResourceResolver>().also {
      whenever(it.getUserLocale()).thenReturn(Locale(region, region))
    }

    return RegionInterceptor(repository, resolver)
  }

  private fun Interceptor.captureInterceptedRequest(chain: Interceptor.Chain): Request {
    intercept(chain)

    val requestCaptor = argumentCaptor<Request>()
    verify(chain).proceed(requestCaptor.capture())

    return requestCaptor.firstValue
  }
}