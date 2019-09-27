package com.illiarb.tmdbclient.storage.local

import com.ironz.binaryprefs.BinaryPreferencesBuilder
import com.ironz.binaryprefs.Preferences
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable

/**
 * @author ilya-rb on 26.10.18.
 */
fun <T : Persistable> BinaryPreferencesBuilder.registerPersistables(
    persistables: Map<String, Class<out T>>
): BinaryPreferencesBuilder = apply {
    persistables.forEach {
        registerPersistable(it.key, it.value)
    }
}

fun <T : Persistable> Preferences.getValue(key: String, result: T): T = getPersistable(key, result)

fun Preferences.putValue(key: String, value: Persistable) =
    edit().putPersistable(key, value).commit()