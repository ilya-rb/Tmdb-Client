package com.tmdbclient.servicetmdb

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.tmdbclient.servicetmdb.cache.TmdbCache
import com.tmdbclient.servicetmdb.configuration.Configuration
import com.tmdbclient.servicetmdb.configuration.ImageConfig
import com.tmdbclient.servicetmdb.configuration.ImageType
import com.tmdbclient.servicetmdb.configuration.ImageUrlCreator
import junit.framework.Assert.assertTrue
import org.junit.Test

class ImageUrlCreatorTest {

    private val cache = mock<TmdbCache>()
    private val imageUrlCreator = ImageUrlCreator(cache)
    private val sizes = listOf("profile/", "backdrop/", "poster/")

    @Test
    fun `should use fallback is config url is empty`() {
        mockConfiguration(createConfiguration(createImageConfig(secureBaseUrl = "")))

        val result = imageUrlCreator.createImageUrl("image_path", ImageType.Backdrop)
        assertTrue(result.startsWith(BuildConfig.IMG_URL))
    }

    @Test
    fun `should contain profile size for profile image type`() {
        mockConfiguration()

        val result = imageUrlCreator.createImageUrl("image_path", ImageType.Profile)
        assertTrue(result.containsOnly("profile/", sizes))
    }

    @Test
    fun `should contain backdrop size for backdrop image type`() {
        mockConfiguration()

        val result = imageUrlCreator.createImageUrl("image_path", ImageType.Backdrop)
        assertTrue(result.containsOnly("backdrop/", sizes))
    }

    @Test
    fun `should contain poster size for poster image type`() {
        mockConfiguration()

        val result = imageUrlCreator.createImageUrl("image_path", ImageType.Poster)
        assertTrue(result.containsOnly("poster/", sizes))
    }

    @Test
    fun `should contain slash if secure url is missing`() {
        val secureUrlWithoutSlash = "https://url.com"
        mockConfiguration(createConfiguration(createImageConfig(secureBaseUrl = secureUrlWithoutSlash)))

        val result = imageUrlCreator.createImageUrl("image_path", ImageType.Poster)
        assertTrue(result.startsWith("$secureUrlWithoutSlash/"))
    }

    private fun String.containsOnly(expected: String, variants: List<String>): Boolean {
        val containsVariants = variants.filter { it != expected }.any { contains(it) }
        if (containsVariants) {
            return false
        }
        return contains(expected)
    }

    private fun mockConfiguration(
        configuration: Configuration = createConfiguration()
    ): Configuration {
        return configuration.also {
            whenever(cache.getConfiguration()).thenReturn(it)
        }
    }

    private fun createConfiguration(imageConfig: ImageConfig = createImageConfig()): Configuration {
        return Configuration(imageConfig)
    }

    private fun createImageConfig(
        secureBaseUrl: String = "https://secure_base_url.com/"
    ): ImageConfig {
        return ImageConfig(
            baseUrl = "http://base.url.com/",
            secureBaseUrl = secureBaseUrl,
            backdropSizes = listOf("backdrop/w500", "backdrop/w300"),
            profileSizes = listOf("profile/w500", "profile/w300"),
            posterSizes = listOf("poster/w500", "poster/w300")
        )
    }
}