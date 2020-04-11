package com.tmdbclient.servicetmdb.internal.network.model

import com.google.gson.annotations.SerializedName

internal data class ProductionCountryModel(

  @SerializedName("name")
  val name: String
)