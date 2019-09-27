package com.tmdbclient.service_tmdb.model

import com.google.gson.annotations.SerializedName

data class BackdropModel(@SerializedName("file_path") val filePath: String)

data class BackdropListModel(@SerializedName("backdrops") val backdrops: List<BackdropModel>)