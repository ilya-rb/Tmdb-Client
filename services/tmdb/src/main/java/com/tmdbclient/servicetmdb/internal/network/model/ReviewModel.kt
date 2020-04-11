package com.tmdbclient.servicetmdb.internal.network.model

import com.google.gson.annotations.SerializedName

internal data class ReviewModel(

  @SerializedName("author")
  val author: String,

  @SerializedName("content")
  val content: String,

  @SerializedName("url")
  val url: String

)