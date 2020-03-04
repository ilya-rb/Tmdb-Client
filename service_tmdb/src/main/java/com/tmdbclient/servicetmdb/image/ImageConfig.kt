package com.tmdbclient.servicetmdb.image

import com.google.gson.annotations.SerializedName
import com.illiarb.tmdbclient.storage.local.readStringList
import com.illiarb.tmdbclient.storage.local.writeStringList
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput

/**
 * @author ilya-rb on 30.11.18.
 */
data class ImageConfig(
  @SerializedName("secure_base_url")
  var secureBaseUrl: String = "",

  @SerializedName("backdrop_sizes")
  var backdropSizes: List<String> = emptyList(),

  @SerializedName("profile_sizes")
  var posterSizes: List<String> = emptyList(),

  @SerializedName("poster_sizes")
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