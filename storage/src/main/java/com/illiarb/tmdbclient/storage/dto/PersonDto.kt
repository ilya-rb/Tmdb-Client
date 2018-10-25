package com.illiarb.tmdbclient.storage.dto

import com.google.gson.annotations.SerializedName

data class PersonDto(
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String,
    @SerializedName("character") var character: String,
    @SerializedName("profile_path") var profilePath: String
)