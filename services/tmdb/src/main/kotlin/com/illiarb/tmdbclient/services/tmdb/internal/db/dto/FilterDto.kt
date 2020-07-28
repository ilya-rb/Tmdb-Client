package com.illiarb.tmdbclient.services.tmdb.internal.db.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.illiarb.tmdbclient.services.tmdb.domain.Filter

@Entity
internal data class FilterDto(
  @PrimaryKey val id: Long = DEFAULT_FILTER_ID,
  @ColumnInfo(name = "filter") val filter: Filter
) {

  companion object {
    const val DEFAULT_FILTER_ID = 1L
  }
}