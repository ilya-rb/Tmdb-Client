package com.illiarb.tmdbclient.services.tmdb.internal.network.mappers

import com.illiarb.tmdbclient.libs.util.Mapper
import com.illiarb.tmdbclient.services.tmdb.domain.Person
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.PersonModel
import javax.inject.Inject

internal class PersonMapper @Inject constructor() : Mapper<PersonModel, Person> {

  override fun map(from: PersonModel): Person =
    Person(
      from.id,
      from.name,
      from.character,
      from.profilePath
    )
}