package com.tmdbclient.servicetmdb.internal.network.model

import com.tmdbclient.servicetmdb.internal.cache.readPersistableList
import com.tmdbclient.servicetmdb.internal.cache.writePersistableList
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput

/**
 * @author ilya-rb on 16.11.18.
 */
internal data class MovieListModel(var movies: List<MovieModel>) : Persistable {

  constructor() : this(emptyList())

  override fun readExternal(input: DataInput) {
    movies = mutableListOf<MovieModel>().also {
      input.readPersistableList(it) { MovieModel() }
    }
  }

  override fun writeExternal(output: DataOutput) {
    output.writePersistableList(movies)
  }

  override fun deepClone(): Persistable = this
}