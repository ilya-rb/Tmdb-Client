package com.illiarb.tmdbclient.storage.dto

import com.google.gson.annotations.SerializedName

data class BackdropDto(@SerializedName("filePath") val filePath: String)

data class BackdropListDto(@SerializedName("backdrops") val backdrops: List<BackdropDto>)