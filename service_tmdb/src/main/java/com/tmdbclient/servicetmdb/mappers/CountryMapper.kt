package com.tmdbclient.servicetmdb.mappers

import com.illiarb.tmdblcient.core.domain.Country
import com.illiarb.tmdblcient.core.util.Mapper
import com.tmdbclient.servicetmdb.model.CountryModel
import javax.inject.Inject

class CountryMapper @Inject constructor() : Mapper<CountryModel, Country> {

  override fun map(from: CountryModel): Country = Country(from.code, from.name)
}