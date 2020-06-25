package com.illiarb.tmdbclient.services.tmdb.internal.mappers

import com.illiarb.tmdbclient.libs.util.Mapper
import com.illiarb.tmdbclient.services.tmdb.api.domain.Person
import com.illiarb.tmdbclient.services.tmdb.internal.model.PersonModel
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