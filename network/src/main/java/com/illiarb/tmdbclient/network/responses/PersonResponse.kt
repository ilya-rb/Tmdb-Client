package com.illiarb.tmdbclient.network.responses

import com.google.gson.annotations.SerializedName

data class PersonResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("character") val character: String,
    @SerializedName("profile_path") val profilePath: String
)