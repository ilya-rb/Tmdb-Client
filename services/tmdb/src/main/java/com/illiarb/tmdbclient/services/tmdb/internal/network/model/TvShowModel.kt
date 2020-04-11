package com.illiarb.tmdbclient.services.tmdb.internal.network.model

import com.google.gson.annotations.SerializedName

internal data class TvShowModel(

  @SerializedName("poster_path")
  val posterPath: String,

  @SerializedName("name")
  val name: String

) : TrendingModel