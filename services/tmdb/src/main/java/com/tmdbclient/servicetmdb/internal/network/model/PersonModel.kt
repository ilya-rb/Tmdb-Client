package com.tmdbclient.servicetmdb.internal.network.model

import com.google.gson.annotations.SerializedName

internal data class PersonModel(

  @SerializedName("id")
  val id: Int,

  @SerializedName("name")
  val name: String,

  @SerializedName("character")
  val character: String,

  @SerializedName("profile_path")
  val profilePath: String

) : TrendingModel