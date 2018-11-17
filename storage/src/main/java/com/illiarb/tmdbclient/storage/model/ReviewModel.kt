package com.illiarb.tmdbclient.storage.model

import com.google.gson.annotations.SerializedName

data class ReviewModel(

    @SerializedName("author")
    val author: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("url")
    val url: String

)