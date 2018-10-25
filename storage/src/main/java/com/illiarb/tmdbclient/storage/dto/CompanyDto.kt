package com.illiarb.tmdbclient.storage.dto

import com.google.gson.annotations.SerializedName

data class CompanyDto(
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String
)