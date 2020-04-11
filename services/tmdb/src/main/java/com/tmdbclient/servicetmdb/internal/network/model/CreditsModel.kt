package com.tmdbclient.servicetmdb.internal.network.model

import com.google.gson.annotations.SerializedName

internal data class CreditsModel(@SerializedName("cast") val cast: List<PersonModel>)