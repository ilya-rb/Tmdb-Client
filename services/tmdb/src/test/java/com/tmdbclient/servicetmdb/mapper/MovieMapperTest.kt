package com.tmdbclient.servicetmdb.mapper

import com.illiarb.tmdbclient.util.Result
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.tmdbclient.servicetmdb.internal.configuration.Configuration
import com.tmdbclient.servicetmdb.internal.image.ImageConfig
import com.tmdbclient.servicetmdb.internal.image.ImageUrlCreator
import com.tmdbclient.servicetmdb.internal.network.mappers.GenreMapper
import com.tmdbclient.servicetmdb.internal.network.mappers.MovieMapper
import com.tmdbclient.servicetmdb.internal.network.mappers.PersonMapper
import com.tmdbclient.servicetmdb.internal.network.mappers.ReviewMapper
import com.tmdbclient.servicetmdb.internal.network.model.BackdropListModel
import com.tmdbclient.servicetmdb.internal.network.model.BackdropModel
import com.tmdbclient.servicetmdb.internal.network.model.MovieModel
import com.tmdbclient.servicetmdb.internal.repository.ConfigurationRepository
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Test

class MovieMapperTest {

  private val secureBaseUrl = "https://base-url.com"
  private val configurationRepository = mock<ConfigurationRepository>()
  private val imageUrlCreator = ImageUrlCreator()
  private val movieMapper = MovieMapper(
    GenreMapper(),
    PersonMapper(),
    ReviewMapper(),
    imageUrlCreator
  )

  @Test
  fun `should contain valid image url for backdrop path`() = runBlockingTest {
    mockConfiguration()

    val configuration = configurationRepository.getConfiguration().unwrap()
    val input = MovieModel(backdropPath = "backdrop_path")
    val result = movieMapper.map(configuration, input)
    assertTrue(result.backdropPath!!.baseUrl.startsWith(secureBaseUrl))
  }

  @Test
  fun `should contain valid image url for poster path`() = runBlockingTest {
    mockConfiguration()

    val input = MovieModel(posterPath = "poster_path")
    val configuration = configurationRepository.getConfiguration().unwrap()
    val result = movieMapper.map(configuration, input)
    assertTrue(result.posterPath!!.baseUrl.startsWith(secureBaseUrl))
  }

  @Test
  fun `should contain valid image url for backdrop images`() = runBlockingTest {
    mockConfiguration()

    val input = MovieModel(images = BackdropListModel(createBackdropList()))
    val configuration = configurationRepository.getConfiguration().unwrap()
    val result = movieMapper.map(configuration, input)

    result.images.forEach {
      assertTrue(it.baseUrl.startsWith(secureBaseUrl))
    }
  }

  private fun createBackdropList(): List<BackdropModel> {
    return mutableListOf<BackdropModel>().apply {
      for (i in 0 until 5) {
        add(BackdropModel(filePath = "file_path"))
      }
    }
  }

  private suspend fun mockConfiguration() {
    whenever(configurationRepository.getConfiguration()).thenReturn(
      Result.Ok(
        Configuration(images = ImageConfig(secureBaseUrl = secureBaseUrl))
      )
    )
  }
}