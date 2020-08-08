package com.illiarb.tmdbclient.services.tmdb.internal.mappers

import com.illiarb.tmdbclient.libs.util.Mapper
import com.illiarb.tmdbclient.services.tmdb.domain.Country
import com.illiarb.tmdbclient.services.tmdb.internal.dto.CountryDto
import javax.inject.Inject

internal class CountryMapper @Inject constructor() : Mapper<CountryDto, Country> {

  override fun map(from: CountryDto): Country = Country(from.code, from.name)
}