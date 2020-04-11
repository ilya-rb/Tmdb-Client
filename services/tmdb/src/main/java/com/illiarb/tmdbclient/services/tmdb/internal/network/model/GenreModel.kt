package com.illiarb.tmdbclient.services.tmdb.internal.network.model

import com.google.gson.annotations.SerializedName
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput
import com.illiarb.tmdbclient.services.tmdb.internal.cache.readPersistableList
import com.illiarb.tmdbclient.services.tmdb.internal.cache.writePersistableList
import java.util.Collections

internal data class GenreModel(
  @SerializedName("id") var id: Int,
  @SerializedName("name") var name: String
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

internal data class GenreListModel(@SerializedName("genres") var genres: List<GenreModel>) :
  Persistable {

  constructor() : this(Collections.emptyList())

  override fun readExternal(input: DataInput) {
    genres = mutableListOf<GenreModel>().also {
      input.readPersistableList(it) { GenreModel() }
    }
  }

  override fun deepClone(): Persistable = this

  override fun writeExternal(output: DataOutput) {
    output.writePersistableList(genres)
  }
}