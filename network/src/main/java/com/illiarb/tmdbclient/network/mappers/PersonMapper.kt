package com.illiarb.tmdbclient.network.mappers

import com.illiarb.tmdbclient.network.responses.PersonResponse
import com.illiarb.tmdblcient.core.entity.Person
import javax.inject.Inject

class PersonMapper @Inject constructor() : Mapper<PersonResponse, Person> {

    override fun map(from: PersonResponse): Person =
        Person(
            from.id,
            from.name,
            from.character,
            from.profilePath
        )
}