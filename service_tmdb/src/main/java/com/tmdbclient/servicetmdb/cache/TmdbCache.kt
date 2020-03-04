package com.tmdbclient.servicetmdb.cache

import android.content.Context
import com.illiarb.tmdbclient.storage.local.getValue
import com.illiarb.tmdbclient.storage.local.putValue
import com.illiarb.tmdbclient.storage.local.registerPersistables
import com.ironz.binaryprefs.BinaryPreferencesBuilder
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.tmdbclient.servicetmdb.configuration.Configuration
import com.tmdbclient.servicetmdb.model.CountryList
import com.tmdbclient.servicetmdb.model.CountryModel
import com.tmdbclient.servicetmdb.model.GenreListModel
import com.tmdbclient.servicetmdb.model.GenreModel
import com.tmdbclient.servicetmdb.model.MovieListModel
import com.tmdbclient.servicetmdb.model.MovieModel

class TmdbCache(context: Context) {

  companion object {
    const val STORE_NAME = "tmdb_cache"
    const val KEY_POPULAR = "popular"
    const val KEY_TOP_RATED = "top_rated"
    const val KEY_NOW_PLAYING = "now_playing"
    const val KEY_UPCOMING = "upcoming"
    const val KEY_CONFIGURATION = "configuration"
    const val KEY_GENRES = "genres"
    const val KEY_COUNTRIES = "countries"
  }

  private val tmdbStore = BinaryPreferencesBuilder(context)
    .name(STORE_NAME)
    .externalStorage(false)
    .registerPersistables(getPersistablesMap())
    .build()

  fun getMoviesByType(type: String): List<MovieModel> =
    tmdbStore.getValue(type, MovieListModel()).movies

  fun getGenres(): List<GenreModel> =
    tmdbStore.getValue(KEY_GENRES, GenreListModel()).genres

  fun storeMovies(type: String, movies: List<MovieModel>) =
    tmdbStore.putValue(type, MovieListModel(movies))

  fun storeGenres(genres: List<GenreModel>) =
    tmdbStore.putValue(KEY_GENRES, GenreListModel(genres))

  fun storeConfiguration(configuration: Configuration) =
    tmdbStore.putValue(KEY_CONFIGURATION, configuration)

  fun getConfiguration(): Configuration =
    tmdbStore.getPersistable(KEY_CONFIGURATION, Configuration())

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