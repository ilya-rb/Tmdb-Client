package com.tmdbclient.servicetmdb.internal.network.mappers

import com.illiarb.tmdbclient.util.Mapper
import com.tmdbclient.servicetmdb.domain.Person
import com.tmdbclient.servicetmdb.internal.network.model.PersonModel
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