package com.illiarb.tmdbclient.storage.model

import com.google.gson.annotations.SerializedName

data class CreditsModel(@SerializedName("cast") val cast: List<PersonModel>)