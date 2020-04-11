package com.illiarb.tmdbclient.services.tmdb.configuation

import com.illiarb.tmdbclient.services.tmdb.internal.image.ImageConfig
import com.illiarb.tmdbclient.services.tmdb.internal.image.ImageType
import com.illiarb.tmdbclient.services.tmdb.internal.image.ImageUrlCreator
import org.junit.Assert.assertTrue
import org.junit.Test

class ImageUrlCreatorTest {

  private val imageUrlCreator = ImageUrlCreator()

  @Test
  fun `should contain profile size for profile image type`() {
    val config = createImageConfig()
    val image = imageUrlCreator.createImage(config, "image_path", ImageType.Profile)

    assertTrue(containsSizeOnly("profile/", image.sizes))
  }

  @Test
  fun `should contain backdrop size for backdrop image type`() {
    val config = createImageConfig()
    val image = imageUrlCreator.createImage(config, "image_path", ImageType.Backdrop)

    assertTrue(containsSizeOnly("backdrop/", image.sizes))
  }

  @Test
  fun `should contain poster size for poster image type`() {
    val config = createImageConfig()
    val image = imageUrlCreator.createImage(config, "image_path", ImageType.Poster)

    assertTrue(containsSizeOnly("poster/", image.sizes))
  }

  @Test
  fun `should contain slash if secure url is missing`() {
    val secureUrlWithoutSlash = "https://url.com"
    val config = createImageConfig(secureBaseUrl = secureUrlWithoutSlash)
    val image = imageUrlCreator.createImage(config, "image_path", ImageType.Poster)

    assertTrue(image.baseUrl.startsWith("$secureUrlWithoutSlash/"))
  }

  private fun containsSizeOnly(expected: String, sizes: List<String>): Boolean =
    sizes.all { it.contains(expected) }

  private fun createImageConfig(
    secureBaseUrl: String = "https://secure_base_url.com/"
  ): ImageConfig {
    return ImageConfig(
      secureBaseUrl = secureBaseUrl,
      backdropSizes = listOf("backdrop/w500", "backdrop/w300"),
      profileSizes = listOf("profile/w500", "profile/w300"),
      posterSizes = listOf("poster/w500", "poster/w300")
    )
  }
}