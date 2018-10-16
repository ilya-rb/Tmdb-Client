package com.illiarb.tmdbclient.network.mappers

import java.util.Collections

interface Mapper<From, To> {

    fun map(from: From): To

    fun mapOrNull(from: From?): To? = if (from == null) null else map(from)

    fun mapList(collection: List<From>?): List<To> {
        if (collection == null || collection.isEmpty()) {
            return Collections.emptyList()
        }

        return collection.map {
            map(it)
        }
    }
}