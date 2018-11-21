package com.illiarb.tmdbclient.storage.network.response

import com.google.gson.annotations.SerializedName

/**
 * @author ilya-rb on 21.11.18.
 */
data class AuthTokenResponse(

    @SerializedName("success")
    val sucess: Boolean,

    @SerializedName("expires_at")
    val expiresAt: String,

    @SerializedName("request_token")
    val requestToken: String
)