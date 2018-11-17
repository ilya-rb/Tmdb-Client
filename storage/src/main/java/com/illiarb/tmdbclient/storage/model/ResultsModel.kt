package com.illiarb.tmdbclient.storage.model

import com.google.gson.annotations.SerializedName

data class ResultsModel<T>(@SerializedName("results") val results: List<T>)