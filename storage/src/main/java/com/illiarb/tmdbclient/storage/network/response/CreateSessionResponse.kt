package com.illiarb.tmdbclient.storage.network.response

import com.google.gson.annotations.SerializedName

/**
 * @author ilya-rb on 21.11.18.
 */
data class CreateSessionResponse(

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("session_id")
    val sessionId: String
)