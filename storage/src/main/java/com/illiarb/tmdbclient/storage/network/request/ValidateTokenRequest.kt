package com.illiarb.tmdbclient.storage.network.request

import com.google.gson.annotations.SerializedName

/**
 * @author ilya-rb on 21.11.18.
 */
data class ValidateTokenRequest(

    @SerializedName("username")
    val username: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("request_token")
    val requestToken: String
)