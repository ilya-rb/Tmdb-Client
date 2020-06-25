package com.illiarb.tmdbclient.services.tmdb.internal.db

import androidx.room.TypeConverter
import com.illiarb.tmdbclient.services.tmdb.api.domain.Filter
import kotlinx.serialization.cbor.Cbor

internal class Converters {

  @TypeConverter
  fun fromByteArray(data: ByteArray?): Filter? {
    if (data == null) {
      return null
    }
    return Cbor.load(Filter.serializer(), data)
  }

  @TypeConverter
  fun toByteArray(filter: Filter?): ByteArray? {
    if (filter == null) {
      return null
    }
    return Cbor.dump(Filter.serializer(), filter)
  }
}