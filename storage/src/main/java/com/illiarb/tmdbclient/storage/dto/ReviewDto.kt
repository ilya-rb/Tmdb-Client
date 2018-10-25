package com.illiarb.tmdbclient.storage.dto

import com.google.gson.annotations.SerializedName

data class ReviewDto(
    @SerializedName("author") val author: String,
    @SerializedName("content") val content: String,
    @SerializedName("url") val url: String
)

data class ReviewListDto(@SerializedName("reviews") val reviews: List<ReviewDto>)