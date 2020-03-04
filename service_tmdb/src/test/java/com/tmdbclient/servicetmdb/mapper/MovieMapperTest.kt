package com.tmdbclient.servicetmdb.mapper

import com.illiarb.tmdblcient.core.util.Result
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.tmdbclient.servicetmdb.configuration.Configuration
import com.tmdbclient.servicetmdb.image.ImageConfig
import com.tmdbclient.servicetmdb.image.ImageUrlCreator
import com.tmdbclient.servicetmdb.mappers.GenreMapper
import com.tmdbclient.servicetmdb.mappers.MovieMapper
import com.tmdbclient.servicetmdb.mappers.PersonMapper
import com.tmdbclient.servicetmdb.mappers.ReviewMapper
import com.tmdbclient.servicetmdb.model.BackdropListModel
import com.tmdbclient.servicetmdb.model.BackdropModel
import com.tmdbclient.servicetmdb.model.MovieModel
import com.tmdbclient.servicetmdb.repository.ConfigurationRepository
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
    configurationRepository,
    imageUrlCreator
  )

  @Test
  fun `should contain valid image url for backdrop path`() = runBlockingTest {
    mockConfiguration()

    val input = MovieModel(backdropPath = "backdrop_path")
    val result = movieMapper.map(input)
    assertTrue(result.backdropPath!!.baseUrl.startsWith(secureBaseUrl))
  }

  @Test
  fun `should contain valid image url for poster path`() = runBlockingTest {
    mockConfiguration()

    val input = MovieModel(posterPath = "poster_path")
    val result = movieMapper.map(input)
    assertTrue(result.posterPath!!.baseUrl.startsWith(secureBaseUrl))
  }

  @Test
  fun `should contain valid image url for backdrop images`() = runBlockingTest {
    mockConfiguration()

    val input = MovieModel(images = BackdropListModel(createBackdropList()))
    val result = movieMapper.map(input)

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
      Result.Success(
        Configuration(images = ImageConfig(secureBaseUrl = secureBaseUrl))
      )
    )
  }
}