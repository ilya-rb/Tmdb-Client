package com.illiarb.tmdbclient.storage.local

import com.illiarb.tmdbclient.storage.dto.MovieDto
import com.illiarb.tmdbclient.storage.dto.MovieListDto
import com.illiarb.tmdbexplorerdi.App
import com.ironz.binaryprefs.BinaryPreferencesBuilder
import com.ironz.binaryprefs.Preferences
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import javax.inject.Inject

/**
 * @author ilya-rb on 26.10.18.
 */
class PersistableStorage @Inject constructor(app: App) {

    companion object {

        const val STORE_NAME = "tmdb_cache"

        const val KEY_POPULAR_MOVIES = "popular"
        const val KEY_MOVIE = "movie"
    }

    val store: Preferences = BinaryPreferencesBuilder(app.getApplication())
        .name(STORE_NAME)
        .registerPersistable(KEY_MOVIE, MovieDto::class.java)
        .registerPersistable(KEY_POPULAR_MOVIES, MovieListDto::class.java)
        .externalStorage(false)
        .build()

    inline fun <reified T : Persistable> getValue(key: String): T = store.getPersistable(key, T::class.java.newInstance()) as T

    fun putValue(key: String, value: Persistable) {
        store.edit()
            .putPersistable(key, value)
            .apply()
    }

    fun clear() {
        store.edit().clear().apply()
    }
}