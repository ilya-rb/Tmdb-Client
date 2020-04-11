package com.illiarb.tmdbclient.services.tmdb

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.illiarb.tmdbclient.services.tmdb.internal.cache.TmdbCache
import com.illiarb.tmdbclient.services.tmdb.internal.configuration.Configuration
import com.illiarb.tmdbclient.services.tmdb.internal.image.ImageConfig
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.GenreModel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TmdbCacheTest {

  private lateinit var cache: TmdbCache

  @Before
  fun before() {
    val context = InstrumentationRegistry.getInstrumentation().context
    cache = TmdbCache(context)
  }

  @After
  fun after() {
    cache.clear()
  }

  @Test
  fun shouldStoreAndReadConfiguration() {
    val writeConfig = createConfiguration()
    assertTrue(cache.storeConfiguration(writeConfig))

    val readConfig = cache.getConfiguration()
    assertEquals(writeConfig, readConfig)
  }

  @Test
  fun shouldClearStorage() {
    val config = createConfiguration()
    val genres = listOf(GenreModel(), GenreModel())

    cache.storeConfiguration(config)
    cache.storeGenres(genres)
    cache.clear()

    // Should remove every key from store
    val keys = cache.keys()
    assertTrue(keys.none { keys.contains(it) })
  }

  @Test
  fun shouldReturnAllKeys() {
    val config = createConfiguration()
    val genres = listOf(GenreModel(), GenreModel())

    cache.storeConfiguration(config)
    cache.storeGenres(genres)

    val keys = cache.keys()

    assertEquals(keys.size, 2)

    assertTrue(keys.contains(TmdbCache.KEY_CONFIGURATION))
    assertTrue(keys.contains(TmdbCache.KEY_GENRES))
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