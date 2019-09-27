package com.tmdbclient.service_tmdb.model

import com.google.gson.annotations.SerializedName

data class ResultsModel<T>(@SerializedName("results") val results: List<T>)