package com.illiarb.tmdbclient.storage.mappers

import com.illiarb.tmdbclient.storage.model.PersonModel
import com.illiarb.tmdblcient.core.domain.entity.Person
import javax.inject.Inject

class PersonMapper @Inject constructor() : Mapper<PersonModel, Person> {
    override fun map(from: PersonModel): Person =
        Person(
            from.id,
            from.name,
            from.character,
            from.profilePath
        )
}