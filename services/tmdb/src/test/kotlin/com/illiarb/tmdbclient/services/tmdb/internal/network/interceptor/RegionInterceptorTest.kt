package com.illiarb.tmdbclient.services.tmdb.internal.network.interceptor

import com.google.common.truth.Truth.assertThat
import com.illiarb.tmdbclient.libs.tools.ResourceResolver
import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.domain.Country
import com.illiarb.tmdbclient.services.tmdb.internal.repository.ConfigurationRepository
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
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
    val interceptor = RegionInterceptor(
      mockk<ConfigurationRepository>().apply {
        coEvery { getCountries() } returns Result.Ok(listOf(Country(region, region)))
      },
      mockk<ResourceResolver>().apply {
        every { getUserLocale() } returns Locale(region, region)
        every { getUserISOCountry() } returns region
      }
    )

    val request = Request.Builder()
      .url("https://api-url.com/request")
      .build()

    val chain = mockk<Interceptor.Chain>().also {
      every { it.request() } returns request
      every { it.proceed(any()) } returns
          Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .message("")
            .code(200)
            .build()
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    interceptor.intercept(chain)

    val requestSlot = slot<Request>()
    verify { chain.proceed(capture(requestSlot)) }
    verify { chain.request() }
    verify { chain.proceed(any()) }
    confirmVerified(chain)

    assertThat(region).isEqualTo(requestSlot.captured.url.queryParameter("region"))
  }
}