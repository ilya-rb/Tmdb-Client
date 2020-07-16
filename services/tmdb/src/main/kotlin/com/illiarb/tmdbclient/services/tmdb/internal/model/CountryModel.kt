package com.illiarb.tmdbclient.services.tmdb.internal.model

import com.illiarb.tmdbclient.services.tmdb.internal.cache.readPersistableList
import com.illiarb.tmdbclient.services.tmdb.internal.cache.writePersistableList
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class CountryModel(
  @Json(name = "iso_3166_1") var code: String = "",
  @Json(name = "english_name") var name: String = ""
) : Persistable {

  override fun readExternal(input: DataInput) {
    code = input.readString()
    name = input.readString()
  }

  override fun deepClone(): Persistable = this

  override fun writeExternal(output: DataOutput) {
    output.writeString(code)
    output.writeString(name)
  }
}

internal data class CountryList(var countries: List<CountryModel> = emptyList()) : Persistable {

  override fun readExternal(input: DataInput) {
    countries = mutableListOf<CountryModel>().also {
      input.readPersistableList(it) { CountryModel() }
    }
  }

  override fun deepClone(): Persistable = this

  override fun writeExternal(output: DataOutput) {
    output.writePersistableList(countries)
  }
}