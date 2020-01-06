package com.tmdbclient.servicetmdb.configuration

import com.tmdbclient.servicetmdb.BuildConfig
import com.tmdbclient.servicetmdb.cache.TmdbCache
import javax.inject.Inject

class ImageUrlCreator @Inject constructor(private val cache: TmdbCache) {

    fun createImageUrl(
        path: String?,
        imageType: ImageType
    ): String {
        val configuration = cache.getConfiguration()
        val urlBuilder = StringBuilder()
        val defaultSize = BuildConfig.IMG_SIZE

        val baseUrl = if (configuration.images.baseUrl.isEmpty()) {
            BuildConfig.IMG_URL
        } else {
            configuration.images.baseUrl
        }

        val size = when (imageType) {
            ImageType.Backdrop ->
                configuration.images.backdropSizes.firstOrNull() ?: defaultSize
            ImageType.Poster ->
                configuration.images.posterSizes.firstOrNull() ?: defaultSize
            ImageType.Profile ->
                configuration.images.profileSizes.firstOrNull() ?: defaultSize
        }

        urlBuilder.append(baseUrl.ensureEndingSlash())
        urlBuilder.append(size.ensureEndingSlash())
        urlBuilder.append(path?.removeStartSlashIfNeeded())

        return urlBuilder.toString()
    }

    private fun String.ensureEndingSlash(): String =
        if (endsWith('/')) this else this.plus('/')

    private fun String.removeStartSlashIfNeeded(): String =
        if (!isNullOrBlank() && startsWith('/')) substring(1) else this
}