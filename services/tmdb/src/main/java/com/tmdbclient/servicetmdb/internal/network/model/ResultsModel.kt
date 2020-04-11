package com.tmdbclient.servicetmdb.internal.network.model

import com.google.gson.annotations.SerializedName

internal data class ResultsModel<T>(@SerializedName("results") val results: List<T>)