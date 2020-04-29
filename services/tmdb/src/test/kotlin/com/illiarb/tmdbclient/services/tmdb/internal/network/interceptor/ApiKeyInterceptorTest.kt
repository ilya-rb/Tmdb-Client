package com.illiarb.tmdbclient.services.tmdb.internal.network.interceptor

import com.google.common.truth.Truth.assertThat
import com.illiarb.tmdbclient.libs.buildconfig.TmdbConfig
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.jupiter.api.Test

class ApiKeyInterceptorTest {

  private val fakeTmdbConfig = object : TmdbConfig {
    override val apiKey: String get() = "api_key"
    override val apiUrl: String get() = "https://api-url.com/"
  }

  @Test
  fun `it should append build config api key as query parameter to request url`() {
    val interceptor = ApiKeyInterceptor(fakeTmdbConfig)
    val chain = mockk<Interceptor.Chain>().also {
      val request = Request.Builder()
        .url("https://api-url.com/endpoint")
        .build()

      every { it.request() } returns request
      every { it.proceed(any()) } returns
          Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .message("")
            .code(200)
            .build()
    }

    interceptor.intercept(chain)

    val requestSlot = slot<Request>()
    verify { chain.proceed(capture(requestSlot)) }
    verify { chain.request() }
    verify { chain.proceed(any()) }

    confirmVerified(chain)

    val apiKeyParam = requestSlot.captured.url.queryParameter(ApiKeyInterceptor.QUERY_PARAM_API_KEY)
    assertThat(apiKeyParam).isEqualTo(fakeTmdbConfig.apiKey)
  }
}