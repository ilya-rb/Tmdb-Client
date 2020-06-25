package com.illiarb.tmdbclient.services.tmdb.internal.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.illiarb.tmdbclient.services.tmdb.api.domain.Filter

@Entity
internal data class FilterEntity(
  @PrimaryKey val id: Long = DEFAULT_FILTER_ID,
  @ColumnInfo(name = "filter") val filter: Filter
) {

  companion object {
    const val DEFAULT_FILTER_ID = 1L
  }
}