package com.illiarb.tmdbclient.services.tmdb.internal.mappers

import com.illiarb.tmdbclient.libs.util.Mapper
import com.illiarb.tmdbclient.services.tmdb.domain.Country
import com.illiarb.tmdbclient.services.tmdb.internal.model.CountryModel
import javax.inject.Inject

internal class CountryMapper @Inject constructor() : Mapper<CountryModel, Country> {

  override fun map(from: CountryModel): Country = Country(from.code, from.name)
}