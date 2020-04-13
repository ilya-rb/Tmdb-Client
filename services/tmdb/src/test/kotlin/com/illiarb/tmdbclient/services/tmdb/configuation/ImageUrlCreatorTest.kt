package com.illiarb.tmdbclient.services.tmdb.configuation

import com.google.common.truth.Correspondence
import com.google.common.truth.Truth.assertThat
import com.illiarb.tmdbclient.services.tmdb.internal.image.ImageConfig
import com.illiarb.tmdbclient.services.tmdb.internal.image.ImageType
import com.illiarb.tmdbclient.services.tmdb.internal.image.ImageUrlCreator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

internal class ImageUrlCreatorTest {

  private val imageUrlCreator = ImageUrlCreator()

  @Nested
  @DisplayName("Given a valid image config")
  inner class ConfigSizesTest {

    private val config = createImageConfig()
    private val containsSubstring = Correspondence.from<String, String>(
      { actual, expected -> actual?.contains(expected ?: "") ?: false },
      "contains"
    )

    @ParameterizedTest
    @EnumSource(ImageType::class)
    fun `it should contain given size for given image type`(type: ImageType) {
      val image = imageUrlCreator.createImage(config, "image_path", type)

      assertThat(image.sizes)
        .comparingElementsUsing(containsSubstring)
        .contains(type.toString())
    }
  }

  @Nested
  @DisplayName("Given an image config with the secure url without slash at the end")
  inner class ConfigSecureUrlTest {

    @ParameterizedTest
    @EnumSource(ImageType::class)
    fun `it should contain slash at the end of the url`(type: ImageType) {
      val urlWithoutSlash = "https://url.com"
      val config = createImageConfig(secureBaseUrl = urlWithoutSlash)
      val image = imageUrlCreator.createImage(config, "image_path", type)

      assertThat(image.baseUrl).startsWith(urlWithoutSlash.plus("/"))
    }
  }

  private fun createImageConfig(
    secureBaseUrl: String = "https://secure_base_url.com/"
  ): ImageConfig {
    return ImageConfig(
      secureBaseUrl = secureBaseUrl,
      backdropSizes = listOf(
        "backdrop/w500",
        "backdrop/w300"
      ),
      profileSizes = listOf(
        "profile/w500",
        "profile/w300"
      ),
      posterSizes = listOf(
        "poster/w500",
        "poster/w300"
      )
    )
  }
}