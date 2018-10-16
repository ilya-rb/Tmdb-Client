package com.illiarb.tmdbclient.network.responses

import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    @SerializedName("author") val author: String,
    @SerializedName("content") val content: String,
    @SerializedName("url") val url: String
)

data class ReviewListResponse(@SerializedName("reviews") val reviews: List<ReviewResponse>)