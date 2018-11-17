package com.illiarb.tmdbclient.storage.model.here

import com.google.gson.annotations.SerializedName

/**
 * @author ilya-rb on 02.11.18.
 */
data class LocationModel(

    @SerializedName("id")
    val id: String,

    @SerializedName("position")
    val position: List<Double>,

    @SerializedName("distance")
    val distance: Int,

    @SerializedName("title")
    val title: String

)