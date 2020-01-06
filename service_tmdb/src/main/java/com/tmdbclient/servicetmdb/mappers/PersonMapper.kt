package com.tmdbclient.servicetmdb.mappers

import com.illiarb.tmdblcient.core.domain.Person
import com.illiarb.tmdblcient.core.util.Mapper
import com.tmdbclient.servicetmdb.model.PersonModel
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