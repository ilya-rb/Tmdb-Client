package com.illiarb.tmdbclient.network.responses

import com.google.gson.annotations.SerializedName

data class CreditsResponse(@SerializedName("cast") val cast: List<PersonResponse>)