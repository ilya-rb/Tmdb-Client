package com.illiarb.tmdbclient.storage.local

import com.illiarb.tmdbclient.storage.model.AccountModel
import com.illiarb.tmdbclient.storage.model.MovieListModel
import com.illiarb.tmdbclient.storage.model.MovieModel
import com.illiarb.tmdbclient.storage.network.api.config.Configuration
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.modules.account.AccountRepository
import com.illiarb.tmdblcient.core.system.Logger
import com.ironz.binaryprefs.BinaryPreferencesBuilder
import com.ironz.binaryprefs.Preferences
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import io.reactivex.Single
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * @author ilya-rb on 26.10.18.
 */
class PersistableStorage @Inject constructor(app: App) {

    companion object {
        const val STORE_NAME = "tmdb_cache"
        const val KEY_SESSION_ID = "session_id"
        const val KEY_ACCOUNT = "account"
        const val KEY_CONFIGURATION = "configuration"
        const val KEY_POPULAR = MovieFilter.TYPE_POPULAR
        const val KEY_UPCOMING = MovieFilter.TYPE_UPCOMING
        const val KEY_NOW_PLAYING = MovieFilter.TYPE_NOW_PLAYING
        const val KEY_TOP_RATED = MovieFilter.TYPE_TOP_RATED
        const val KEY_LAST_LOADED_MOVIE_DETAILS = "movie_details"
    }

    private val store: Preferences = BinaryPreferencesBuilder(app.getApplication())
        .name(STORE_NAME)
        .registerPersistables()
        .exceptionHandler { Logger.e("Error while interacting with storage", it) }
        .externalStorage(false)
        .build()

    fun isAuthorized(): Boolean = store.getString(KEY_SESSION_ID, null) != null

    fun getMoviesByType(type: String): Single<List<MovieModel>> = Single.just(getValue(type, MovieListModel()).movies)

    suspend fun getCurrentAccountDeferred(): Deferred<AccountModel> = coroutineScope {
        async { getValue(KEY_ACCOUNT, AccountModel()) }
    }

    fun getCurrentAccount(): Single<AccountModel> = Single.just(getValue(KEY_ACCOUNT, AccountModel()))

    fun getSessionId(): String = store.getString(KEY_SESSION_ID, null) ?: ""

    fun storeMovies(type: String, movies: List<MovieModel>) = putValue(type, MovieListModel(movies))

    fun storeAccount(accountModel: AccountModel) = putValue(KEY_ACCOUNT, accountModel)

    fun storeSessionId(sessionId: String) = store.edit().putString(KEY_SESSION_ID, sessionId).commit()

    fun storeConfiguration(configuration: Configuration) = putValue(KEY_CONFIGURATION, configuration)

    fun clearAccountData() {
        store.edit()
            .remove(KEY_SESSION_ID)
            .putPersistable(KEY_ACCOUNT, AccountModel.createNonExistent())
            .commit()
    }

    private fun <T : Persistable> getValue(key: String, result: T): T = store.getPersistable(key, result) as T

    private fun putValue(key: String, value: Persistable) = store.edit().putPersistable(key, value).commit()

    private fun BinaryPreferencesBuilder.registerPersistables(): BinaryPreferencesBuilder {
        registerPersistable(KEY_POPULAR, MovieListModel::class.java)
        registerPersistable(KEY_UPCOMING, MovieListModel::class.java)
        registerPersistable(KEY_NOW_PLAYING, MovieListModel::class.java)
        registerPersistable(KEY_TOP_RATED, MovieListModel::class.java)
        registerPersistable(KEY_ACCOUNT, AccountModel::class.java)
        registerPersistable(KEY_CONFIGURATION, Configuration::class.java)
        registerPersistable(KEY_LAST_LOADED_MOVIE_DETAILS, MovieModel::class.java)
        return this
    }
}