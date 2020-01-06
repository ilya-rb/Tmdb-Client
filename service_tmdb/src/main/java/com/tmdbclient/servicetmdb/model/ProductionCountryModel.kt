package com.tmdbclient.servicetmdb.model

import com.google.gson.annotations.SerializedName

data class ProductionCountryModel(

    @SerializedName("name")
    val name: String
)