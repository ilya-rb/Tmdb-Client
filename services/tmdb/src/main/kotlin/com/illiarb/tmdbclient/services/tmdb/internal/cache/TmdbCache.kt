package com.illiarb.tmdbclient.services.tmdb.internal.cache

import android.content.Context
import com.illiarb.tmdbclient.services.tmdb.internal.dto.ConfigurationDto
import com.illiarb.tmdbclient.services.tmdb.internal.dto.CountryDto
import com.illiarb.tmdbclient.services.tmdb.internal.dto.CountryList
import com.illiarb.tmdbclient.services.tmdb.internal.dto.GenreDto
import com.illiarb.tmdbclient.services.tmdb.internal.dto.GenreListDto
import com.illiarb.tmdbclient.services.tmdb.internal.dto.MovieDto
import com.illiarb.tmdbclient.services.tmdb.internal.dto.MovieListDto
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

  fun getMoviesByType(type: String): MovieListDto =
    tmdbStore.getValue(type, MovieListDto())

  fun getGenres(): List<GenreDto> =
    tmdbStore.getValue(KEY_GENRES, GenreListDto()).genres

  fun storeMovies(type: String, movies: List<MovieDto>) =
    tmdbStore.putValue(type, MovieListDto(movies))

  fun storeGenres(genres: List<GenreDto>) =
    tmdbStore.putValue(KEY_GENRES, GenreListDto(genres))

  fun storeConfiguration(configuration: ConfigurationDto) =
    tmdbStore.putValue(KEY_CONFIGURATION, configuration)

  fun updateConfigurationTimestamp(time: Long) {
    tmdbStore.edit().putLong(KEY_CONFIGURATION_LAST_UPDATE, time).commit()
  }

  fun getConfiguration(): ConfigurationDto =
    tmdbStore.getPersistable(KEY_CONFIGURATION, ConfigurationDto())

  fun getConfigurationLastUpdateTimestamp(): Long =
    tmdbStore.getLong(KEY_CONFIGURATION_LAST_UPDATE, 0)

  fun getCountries(): List<CountryDto> =
    tmdbStore.getPersistable(KEY_COUNTRIES, CountryList()).countries

  fun storeCountries(countries: List<CountryDto>) =
    tmdbStore.putValue(KEY_COUNTRIES, CountryList(countries))

  fun clear() {
    tmdbStore.edit().clear().commit()
  }

  fun keys(): Set<String> = tmdbStore.keys()

  private fun getPersistablesMap(): Map<String, Class<out Persistable>> =
    mapOf(
      KEY_POPULAR to MovieListDto::class.java,
      KEY_TOP_RATED to MovieListDto::class.java,
      KEY_UPCOMING to MovieListDto::class.java,
      KEY_NOW_PLAYING to MovieListDto::class.java,
      KEY_CONFIGURATION to ConfigurationDto::class.java,
      KEY_GENRES to GenreListDto::class.java,
      KEY_COUNTRIES to CountryList::class.java
    )
}