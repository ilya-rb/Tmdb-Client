package com.tmdbclient.servicetmdb.mapper

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.tmdbclient.servicetmdb.BuildConfig
import com.tmdbclient.servicetmdb.cache.TmdbCache
import com.tmdbclient.servicetmdb.configuration.Configuration
import com.tmdbclient.servicetmdb.configuration.ImageConfig
import com.tmdbclient.servicetmdb.configuration.ImageUrlCreator
import com.tmdbclient.servicetmdb.mappers.GenreMapper
import com.tmdbclient.servicetmdb.mappers.MovieMapper
import com.tmdbclient.servicetmdb.mappers.PersonMapper
import com.tmdbclient.servicetmdb.mappers.ReviewMapper
import com.tmdbclient.servicetmdb.model.BackdropListModel
import com.tmdbclient.servicetmdb.model.BackdropModel
import com.tmdbclient.servicetmdb.model.MovieModel
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MovieMapperTest {

    private val cache = mock<TmdbCache>()
    private val imageUrlCreator = ImageUrlCreator(cache)

    private val movieMapper = MovieMapper(
        GenreMapper(),
        PersonMapper(),
        ReviewMapper(),
        imageUrlCreator
    )

    @Before
    fun setup() {
        mockConfiguration()
    }

    @Test
    fun `should contain valid image url for backdrop path`() {
        val input = MovieModel(backdropPath = "backdrop_path")
        val result = movieMapper.map(input)
        assertTrue(result.backdropPath!!.startsWith(BuildConfig.IMG_URL))
    }

    @Test
    fun `should contain valid image url for poster path`() {
        val input = MovieModel(posterPath = "poster_path")
        val result = movieMapper.map(input)
        assertTrue(result.posterPath!!.startsWith(BuildConfig.IMG_URL))
    }

    @Test
    fun `should contain valid image url for backdrop images`() {
        val input = MovieModel(images = BackdropListModel(createBackdropList()))
        val result = movieMapper.map(input)

        result.images.forEach {
            assertTrue(it.startsWith(BuildConfig.IMG_URL))
        }
    }

    private fun createBackdropList(): List<BackdropModel> {
        return mutableListOf<BackdropModel>().apply {
            for (i in 0 until 5) {
                add(BackdropModel(filePath = "file_path"))
            }
        }
    }

    private fun mockConfiguration() {
        whenever(cache.getConfiguration())
            .thenReturn(Configuration(images = ImageConfig(secureBaseUrl = BuildConfig.IMG_URL)))
    }
}