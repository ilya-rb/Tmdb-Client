package com.illiarb.tmdbclient.services.tmdb.internal.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.illiarb.tmdbclient.services.tmdb.internal.db.dto.FilterDto
import kotlinx.coroutines.flow.Flow

@Dao
internal interface FilterDao {

  @Query("SELECT * FROM FilterDto WHERE ID = ${FilterDto.DEFAULT_FILTER_ID}")
  fun filter(): Flow<FilterDto?>

  @Query("SELECT * FROM FilterDto WHERE ID = ${FilterDto.DEFAULT_FILTER_ID}")
  fun getFilter(): FilterDto?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun storeFilter(filter: FilterDto)
}