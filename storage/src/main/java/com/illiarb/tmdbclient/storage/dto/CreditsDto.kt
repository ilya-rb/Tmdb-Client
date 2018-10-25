package com.illiarb.tmdbclient.storage.dto

import com.google.gson.annotations.SerializedName

data class CreditsDto(@SerializedName("cast") val cast: List<PersonDto>)