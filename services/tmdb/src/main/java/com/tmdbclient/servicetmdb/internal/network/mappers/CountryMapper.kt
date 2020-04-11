package com.tmdbclient.servicetmdb.internal.network.mappers

import com.illiarb.tmdbclient.util.Mapper
import com.tmdbclient.servicetmdb.domain.Country
import com.tmdbclient.servicetmdb.internal.network.model.CountryModel
import javax.inject.Inject

internal class CountryMapper @Inject constructor() : Mapper<CountryModel, Country> {

  override fun map(from: CountryModel): Country = Country(from.code, from.name)
}