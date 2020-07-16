package com.illiarb.tmdbclient.services.tmdb.internal.model

import com.illiarb.tmdbclient.services.tmdb.internal.cache.readPersistableList
import com.illiarb.tmdbclient.services.tmdb.internal.cache.writePersistableList
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput
import java.util.concurrent.TimeUnit

/**
 * @author ilya-rb on 16.11.18.
 */
internal data class MovieListModel(
  var movies: List<MovieModel>,
  var createdAt: Long = System.currentTimeMillis()
) : Persistable {

  companion object {
    const val EXPIRY_DURATION_DAYS = 1
  }

  constructor() : this(emptyList())

  override fun readExternal(input: DataInput) {
    createdAt = input.readLong()
    movies = mutableListOf<MovieModel>().also {
      input.readPersistableList(it) { MovieModel() }
    }
  }

  override fun writeExternal(output: DataOutput) {
    output.writeLong(createdAt)
    output.writePersistableList(movies)
  }

  override fun deepClone(): Persistable = this

  fun isExpired(): Boolean =
    TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - createdAt) > EXPIRY_DURATION_DAYS
}