package com.tmdbclient.service_tmdb.model

import com.google.gson.annotations.SerializedName

data class PersonModel(

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("character")
    val character: String,

    @SerializedName("profile_path")
    val profilePath: String

)