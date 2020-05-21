package com.illiarb.tmdbclient.services.tmdb.internal.configuration

import com.illiarb.tmdbclient.services.tmdb.internal.cache.readStringList
import com.illiarb.tmdbclient.services.tmdb.internal.cache.writeStringList
import com.illiarb.tmdbclient.services.tmdb.internal.image.ImageConfig
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Collections

/**
 * @author ilya-rb on 30.11.18.
 */
@JsonClass(generateAdapter = true)
internal data class Configuration(

  @Json(name = "images")
  var images: ImageConfig = ImageConfig(),

  @Json(name = "change_keys")
  var changeKeys: List<String> = Collections.emptyList()

) : Persistable {

  override fun readExternal(input: DataInput) {
    images.readExternal(input)
    changeKeys = mutableListOf<String>().also { input.readStringList(it) }
  }

  override fun deepClone(): Persistable = this

  override fun writeExternal(output: DataOutput) {
    images.writeExternal(output)
    output.writeStringList(changeKeys)
  }

  fun isNotEmpty(): Boolean = images.secureBaseUrl.isNotEmpty()
      && images.backdropSizes.isNotEmpty()
      && images.posterSizes.isNotEmpty()
      && images.profileSizes.isNotEmpty()
      && changeKeys.isNotEmpty()
}