package com.illiarb.tmdbclient.storage.local

import android.util.Log
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

    private val store: Preferences = BinaryPreferencesBuilder(app.getApplication())
        .name(STORE_NAME)
        .registerPersistable(KEY_MOVIE, MovieDto::class.java)
        .registerPersistable(KEY_POPULAR_MOVIES, MovieListDto::class.java)
        .exceptionHandler { Log.e(PersistableStorage::class.java.name, "Cache error", it) }
        .externalStorage(false)
        .build()

    fun <T : Persistable> getValue(key: String, result: T): T = store.getPersistable(key, result) as T

    fun putValue(key: String, value: Persistable) {
        store.edit()
            .putPersistable(key, value)
            .commit()
    }

    fun clear() {
        store.edit().clear().apply()
    }
}