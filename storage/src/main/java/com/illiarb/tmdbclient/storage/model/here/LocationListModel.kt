package com.illiarb.tmdbclient.storage.model.here

import com.google.gson.annotations.SerializedName

/**
 * @author ilya-rb on 16.11.18.
 */
data class LocationListModel(

    @SerializedName("next")
    val next: String,

    @SerializedName("items")
    val items: List<LocationModel>

)