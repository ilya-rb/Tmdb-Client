package com.tmdbclient.service_tmdb.model

import com.google.gson.annotations.SerializedName

data class ReviewModel(

    @SerializedName("author")
    val author: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("url")
    val url: String

)