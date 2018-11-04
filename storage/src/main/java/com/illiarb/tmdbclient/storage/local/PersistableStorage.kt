package com.illiarb.tmdbclient.storage.local

import android.util.Log
import com.illiarb.tmdbclient.storage.dto.MovieListDto
import com.illiarb.tmdbexplorerdi.App
import com.illiarb.tmdblcient.core.entity.Movie
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
    }

    private val store: Preferences = BinaryPreferencesBuilder(app.getApplication())
        .name(STORE_NAME)
        .registerPersistable(Movie.TYPE_POPULAR, MovieListDto::class.java)
        .registerPersistable(Movie.TYPE_UPCOMING, MovieListDto::class.java)
        .registerPersistable(Movie.TYPE_NOW_PLAYING, MovieListDto::class.java)
        .exceptionHandler { Log.e(PersistableStorage::class.java.name, "Cache error", it) }
        .externalStorage(false)
        .build()

    fun <T : Persistable> getValue(key: String, result: T): T = store.getPersistable(key, result) as T

    fun putValue(key: String, value: Persistable) {
        store.edit()
            .putPersistable(key, value)
            .commit()
    }
}