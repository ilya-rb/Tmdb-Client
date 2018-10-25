package com.illiarb.tmdbclient.storage.mappers

import com.illiarb.tmdbclient.storage.dto.PersonDto
import com.illiarb.tmdblcient.core.entity.Person
import javax.inject.Inject

class PersonMapper @Inject constructor() : Mapper<PersonDto, Person> {

    override fun map(from: PersonDto): Person =
        Person(
            from.id,
            from.name,
            from.character,
            from.profilePath
        )
}