package com.illiarb.tmdbclient.services.tmdb.util

object TmdbImageSizeSelector {

  private val imageSizePattern = "w(\\d+)$".toRegex()
  private val imageSizeHeightPattern = "h(\\d+)$".toRegex()

  /**
   * Source:
   * https://github.com/chrisbanes/tivi/blob/master/tmdb/src/main/java/app/tivi/tmdb/TmdbImageUrlProvider.kt
   */
  @Suppress("ComplexMethod", "ReturnCount", "NestedBlockDepth")
  fun selectSize(sizes: List<String>, imageWidth: Int, imageHeight: Int): String? {
    var previousSize: String? = null

    var previousWidth = 0
    var previousHeight = 0

    for (i in sizes.indices) {
      val size = sizes[i]

      val sizeWidth = extractWidthAsIntFrom(size)
      val sizeHeight = extractHeightAsIntFrom(size)

      if (sizeWidth != null) {
        if (sizeWidth > imageWidth) {
          if (previousSize != null && imageWidth > (previousWidth + sizeWidth) / 2) {
            return size
          } else if (previousSize != null) {
            return previousSize
          }
        } else if (i == sizes.size - 1) {
          // If we get here then we're larger than the last bucket
          if (imageWidth < sizeWidth * 2) {
            return size
          }
        }
        previousWidth = sizeWidth
      } else if (sizeHeight != null) {
        if (sizeHeight > imageHeight) {
          if (previousSize != null && imageHeight > (previousHeight + sizeHeight) / 2) {
            return size
          } else if (previousSize != null) {
            return previousSize
          }
        } else if (i == sizes.size - 1) {
          // If we get here then we're larger than the last bucket
          if (imageHeight < sizeHeight * 2) {
            return size
          }
        }
        previousHeight = sizeHeight
      }
      previousSize = size
    }

    return previousSize ?: if (sizes.isNotEmpty()) sizes.last() else null
  }

  private fun extractHeightAsIntFrom(size: String): Int? {
    return imageSizeHeightPattern.matchEntire(size)?.groups?.get(1)?.value?.toInt()
  }

  private fun extractWidthAsIntFrom(size: String): Int? {
    return imageSizePattern.matchEntire(size)?.groups?.get(1)?.value?.toInt()
  }

}