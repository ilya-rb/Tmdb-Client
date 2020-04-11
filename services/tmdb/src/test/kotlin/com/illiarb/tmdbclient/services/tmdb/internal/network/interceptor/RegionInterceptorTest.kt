package com.illiarb.tmdbclient.services.tmdb.internal.network.interceptor

import com.illiarb.tmdbclient.libs.tools.ResourceResolver
import com.illiarb.tmdbclient.libs.util.Result
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.illiarb.tmdbclient.services.tmdb.domain.Country
import com.illiarb.tmdbclient.services.tmdb.internal.repository.ConfigurationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.Interceptor
import okhttp3.Request
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Locale

@ExperimentalCoroutinesApi
class RegionInterceptorTest {

  @Test
  fun `it should append user region as query parameter`() = runBlockingTest {
    val region = "UA"
    val interceptor = createInterceptorWithRegion(region)

    val chain = mock<Interceptor.Chain>().also {
      whenever(it.request()).thenReturn(
        Request.Builder()
          .url("https://api-url.com/request")
          .build()
      )
    }

    val request = interceptor.captureInterceptedRequest(chain)
    assertEquals(region, request.url.queryParameter("region"))
  }

  private suspend fun createInterceptorWithRegion(region: String): RegionInterceptor {
    val repository = mock<ConfigurationRepository>().also {
      whenever(it.getCountries()).thenReturn(
        Result.Ok(
          listOf(
            Country(region, region)
          )
        )
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