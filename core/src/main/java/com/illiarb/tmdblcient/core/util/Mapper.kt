package com.illiarb.tmdblcient.core.util

import java.util.Collections

interface Mapper<From, To> {

    fun map(from: From): To

    fun mapList(collection: List<From>?): List<To> {
        if (collection == null || collection.isEmpty()) {
            return Collections.emptyList()
        }

        return collection.map {
            map(it)
        }
    }
}