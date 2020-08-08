package com.illiarb.tmdbclient.services.tmdb.internal.dto

import com.illiarb.tmdbclient.services.tmdb.internal.cache.readPersistableList
import com.illiarb.tmdbclient.services.tmdb.internal.cache.writePersistableList
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Collections

@JsonClass(generateAdapter = true)
internal data class GenreDto(
  @Json(name = "id") var id: Int,
  @Json(name = "name") var name: String
) : Persistable {

  constructor() : this(0, "")

  override fun readExternal(input: DataInput) {
    id = input.readInt()
    name = input.readString()
  }

  override fun deepClone(): Persistable = this

  override fun writeExternal(output: DataOutput) {
    output.writeInt(id)
    output.writeString(name)
  }
}

@JsonClass(generateAdapter = true)
internal data class GenreListDto(
  @Json(name = "genres") var genres: List<GenreDto>
) : Persistable {

  constructor() : this(Collections.emptyList())

  override fun readExternal(input: DataInput) {
    genres = mutableListOf<GenreDto>().also {
      input.readPersistableList(it) { GenreDto() }
    }
  }

  override fun deepClone(): Persistable = this

  override fun writeExternal(output: DataOutput) {
    output.writePersistableList(genres)
  }
}