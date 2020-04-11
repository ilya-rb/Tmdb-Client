package com.tmdbclient.servicetmdb.internal.image

import com.tmdbclient.servicetmdb.domain.Image
import javax.inject.Inject

internal class ImageUrlCreator @Inject constructor() {

  fun createImage(imageConfig: ImageConfig, path: String?, imageType: ImageType): Image {
    if (path == null) {
      return Image("", "", emptyList())
    }

    val sizes = when (imageType) {
      ImageType.Backdrop -> imageConfig.backdropSizes
      ImageType.Poster -> imageConfig.posterSizes
      ImageType.Profile -> imageConfig.profileSizes
    }

    return Image(imageConfig.secureBaseUrl.ensureEndingSlash(), path, sizes)
  }

  private fun String.ensureEndingSlash(): String = if (endsWith('/')) this else this.plus('/')
}