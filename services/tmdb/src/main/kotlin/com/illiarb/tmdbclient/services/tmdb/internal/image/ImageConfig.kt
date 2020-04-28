package com.illiarb.tmdbclient.services.tmdb.internal.image

import com.illiarb.tmdbclient.services.tmdb.internal.cache.readStringList
import com.illiarb.tmdbclient.services.tmdb.internal.cache.writeStringList
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author ilya-rb on 30.11.18.
 */
@JsonClass(generateAdapter = true)
internal data class ImageConfig(
  @Json(name = "secure_base_url")
  var secureBaseUrl: String = "",

  @Json(name = "backdrop_sizes")
  var backdropSizes: List<String> = emptyList(),

  @Json(name = "profile_sizes")
  var posterSizes: List<String> = emptyList(),

  @Json(name = "poster_sizes")
  var profileSizes: List<String> = emptyList()
) : Persistable {

  override fun readExternal(input: DataInput) {
    secureBaseUrl = input.readString()
    backdropSizes = mutableListOf<String>().also { input.readStringList(it) }
    posterSizes = mutableListOf<String>().also { input.readStringList(it) }
    profileSizes = mutableListOf<String>().also { input.readStringList(it) }
  }

  override fun writeExternal(output: DataOutput) {
    output.writeString(secureBaseUrl)
    output.writeStringList(backdropSizes)
    output.writeStringList(posterSizes)
    output.writeStringList(profileSizes)
  }

  override fun deepClone(): Persistable = this
}