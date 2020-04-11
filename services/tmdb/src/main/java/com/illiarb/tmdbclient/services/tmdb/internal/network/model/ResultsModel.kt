package com.illiarb.tmdbclient.services.tmdb.internal.network.model

import com.google.gson.annotations.SerializedName

internal data class ResultsModel<T>(@SerializedName("results") val results: List<T>)