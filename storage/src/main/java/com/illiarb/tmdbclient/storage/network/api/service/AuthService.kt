package com.illiarb.tmdbclient.storage.network.api.service

import com.illiarb.tmdbclient.storage.network.request.CreateSessionRequest
import com.illiarb.tmdbclient.storage.network.request.ValidateTokenRequest
import com.illiarb.tmdbclient.storage.network.response.AuthTokenResponse
import com.illiarb.tmdbclient.storage.network.response.CreateSessionResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * @author ilya-rb on 21.11.18.
 */
interface AuthService {

    @GET("authentication/token/new")
    fun requestAuthTokenAsync(): Deferred<AuthTokenResponse>

    @POST("authentication/session/new")
    fun createNewSessionAsync(@Body request: CreateSessionRequest): Deferred<CreateSessionResponse>

    @POST("authentication/token/validate_with_login")
    fun validateTokenWithCredentialsAsync(
        @Body request: ValidateTokenRequest
    ): Deferred<AuthTokenResponse>
}