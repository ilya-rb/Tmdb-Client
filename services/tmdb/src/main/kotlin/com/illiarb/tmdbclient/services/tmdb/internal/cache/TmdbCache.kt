package com.illiarb.tmdbclient.services.tmdb.internal.cache

import android.content.Context
import com.illiarb.tmdbclient.services.tmdb.internal.model.Configuration
import com.illiarb.tmdbclient.services.tmdb.internal.model.CountryList
import com.illiarb.tmdbclient.services.tmdb.internal.model.CountryModel
import com.illiarb.tmdbclient.services.tmdb.internal.model.GenreListModel
import com.illiarb.tmdbclient.services.tmdb.internal.model.GenreModel
import com.illiarb.tmdbclient.services.tmdb.internal.model.MovieListModel
import com.illiarb.tmdbclient.services.tmdb.internal.model.MovieModel
import com.ironz.binaryprefs.BinaryPreferencesBuilder
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable

internal class TmdbCache(context: Context) {

  companion object {
    const val STORE_NAME = "tmdb_cache"
    const val KEY_POPULAR = "popular"
    const val KEY_TOP_RATED = "top_rated"
    const val KEY_NOW_PLAYING = "now_playing"
    const val KEY_UPCOMING = "upcoming"
    const val KEY_CONFIGURATION = "configuration"
    const val KEY_GENRES = "genres"
    const val KEY_COUNTRIES = "countries"
    const val KEY_CONFIGURATION_LAST_UPDATE = "configuration_last_update"
  }

  private val tmdbStore = BinaryPreferencesBuilder(context)
    .name(STORE_NAME)
    .externalStorage(false)
    .registerPersistables(getPersistablesMap())
    .build()

  fun getMoviesByType(type: String): MovieListModel =
    tmdbStore.getValue(type, MovieListModel())

  fun getGenres(): List<GenreModel> =
    tmdbStore.getValue(KEY_GENRES, GenreListModel()).genres

  fun storeMovies(type: String, movies: List<MovieModel>) =
    tmdbStore.putValue(type, MovieListModel(movies))

  fun storeGenres(genres: List<GenreModel>) =
    tmdbStore.putValue(KEY_GENRES, GenreListModel(genres))

  fun storeConfiguration(configuration: Configuration) =
    tmdbStore.putValue(KEY_CONFIGURATION, configuration)

  fun updateConfigurationTimestamp(time: Long) {
    tmdbStore.edit().putLong(KEY_CONFIGURATION_LAST_UPDATE, time).commit()
  }

  fun getConfiguration(): Configuration =
    tmdbStore.getPersistable(KEY_CONFIGURATION, Configuration())

  fun getConfigurationLastUpdateTimestamp(): Long =
    tmdbStore.getLong(KEY_CONFIGURATION_LAST_UPDATE, 0)

  fun getCountries(): List<CountryModel> =
    tmdbStore.getPersistable(KEY_COUNTRIES, CountryList()).countries

  fun storeCountries(countries: List<CountryModel>) =
    tmdbStore.putValue(KEY_COUNTRIES, CountryList(countries))

  fun clear() {
    tmdbStore.edit().clear()
  }

  fun keys(): Set<String> = tmdbStore.keys()

  private fun getPersistablesMap(): Map<String, Class<out Persistable>> =
    mapOf(
      KEY_POPULAR to MovieListModel::class.java,
      KEY_TOP_RATED to MovieListModel::class.java,
      KEY_UPCOMING to MovieListModel::class.java,
      KEY_NOW_PLAYING to MovieListModel::class.java,
      KEY_CONFIGURATION to Configuration::class.java,
      KEY_GENRES to GenreListModel::class.java,
      KEY_COUNTRIES to CountryList::class.java
    )
}