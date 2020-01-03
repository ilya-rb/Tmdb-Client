package com.tmdbclient.service_tmdb.model

import com.google.gson.annotations.SerializedName

data class TvShowModel(

    @SerializedName("poster_path")
    val posterPath: String,

    @SerializedName("name")
    val name: String

) : TrendingModel