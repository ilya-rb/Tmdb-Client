package com.illiarb.tmdbclient.network.responses

import com.google.gson.annotations.SerializedName

data class BackdropResponse(@SerializedName("filePath") val filePath: String)

data class BackdropListResponse(@SerializedName("backdrops") val backdrops: List<BackdropResponse>)