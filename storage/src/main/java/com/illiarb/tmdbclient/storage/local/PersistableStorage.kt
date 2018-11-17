package com.illiarb.tmdbclient.storage.local

import com.illiarb.tmdbclient.storage.model.MovieListModel
import com.illiarb.tmdbexplorerdi.App
import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.system.Logger
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
        .registerPersistable(MovieFilter.TYPE_POPULAR, MovieListModel::class.java)
        .registerPersistable(MovieFilter.TYPE_UPCOMING, MovieListModel::class.java)
        .registerPersistable(MovieFilter.TYPE_NOW_PLAYING, MovieListModel::class.java)
        .exceptionHandler { Logger.e("Error while interacting with storage", it) }
        .externalStorage(false)
        .build()

    fun <T : Persistable> getValue(key: String, result: T): T = store.getPersistable(key, result) as T

    fun putValue(key: String, value: Persistable) {
        store.edit()
            .putPersistable(key, value)
            .commit()
    }
}