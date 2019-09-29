package com.tmdbclient.service_tmdb.model

import com.google.gson.annotations.SerializedName

data class GenreModel(

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String

)

data class GenreListModel(@SerializedName("genres") val genres: List<GenreModel>)