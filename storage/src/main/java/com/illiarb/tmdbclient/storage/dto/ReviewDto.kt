package com.illiarb.tmdbclient.storage.dto

import com.google.gson.annotations.SerializedName

data class ReviewDto(
    @SerializedName("author") var author: String,
    @SerializedName("content") var content: String,
    @SerializedName("url") var url: String
)

data class ReviewListDto(@SerializedName("reviews") val reviews: List<ReviewDto>)