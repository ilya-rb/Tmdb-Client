package com.illiarb.tmdbclient.services.tmdb

import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.illiarb.tmdbclient.services.tmdb.internal.cache.TmdbCache
import com.illiarb.tmdbclient.services.tmdb.internal.model.Configuration
import com.illiarb.tmdbclient.services.tmdb.internal.image.ImageConfig
import com.illiarb.tmdbclient.services.tmdb.internal.model.GenreModel
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class TmdbCacheTest {

  private lateinit var cache: TmdbCache

  @BeforeEach
  fun beforeEach() {
    val context = InstrumentationRegistry.getInstrumentation().context
    cache = TmdbCache(context)
  }

  @AfterEach
  fun afterEach() {
    cache.clear()
  }

  @Test
  @DisplayName("it should store and read the configuration")
  fun readWriteConfigurationTest() {
    val writeConfig = createConfiguration()
    assertThat(cache.storeConfiguration(writeConfig)).isTrue()

    val readConfig = cache.getConfiguration()
    assertThat(writeConfig).isEqualTo(readConfig)
  }

  @Test
  @DisplayName("it should clear the storage")
  fun clearStorageTest() {
    val config = createConfiguration()
    val genres = listOf(GenreModel(), GenreModel())

    cache.storeConfiguration(config)
    cache.storeGenres(genres)
    cache.clear()

    // Should remove every key from store
    val keys = cache.keys()
    assertThat(keys).isEmpty()
  }

  @Test
  @DisplayName("it should return all keys")
  fun returnKeysTest() {
    val config = createConfiguration()
    val genres = listOf(GenreModel(), GenreModel())

    cache.storeConfiguration(config)
    cache.storeGenres(genres)

    val keys = cache.keys()

    assertThat(keys.size).isEqualTo(2)
    assertThat(keys).contains(TmdbCache.KEY_CONFIGURATION)
    assertThat(keys).contains(TmdbCache.KEY_GENRES)
  }

  private fun createConfiguration() =
    Configuration(
      changeKeys = listOf("movies", "reviews", "images"),
      images = ImageConfig(
        secureBaseUrl = "secure_base_url",
        backdropSizes = listOf("100", "200", "300"),
        posterSizes = listOf("100", "200", "300"),
        profileSizes = listOf("100", "200", "300")
      )
    )
}