package com.illiarb.tmdbclient.services.tmdb.internal.network.model

import com.google.gson.annotations.SerializedName

internal data class BackdropModel(@SerializedName("file_path") val filePath: String)

internal data class BackdropListModel(@SerializedName("backdrops") val backdrops: List<BackdropModel>)