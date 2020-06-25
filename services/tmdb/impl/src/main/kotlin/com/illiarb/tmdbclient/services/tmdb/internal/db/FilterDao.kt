package com.illiarb.tmdbclient.services.tmdb.internal.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.illiarb.tmdbclient.services.tmdb.internal.db.entity.FilterEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface FilterDao {

  @Query("SELECT * FROM FilterEntity WHERE ID = ${FilterEntity.DEFAULT_FILTER_ID}")
  fun filter(): Flow<FilterEntity?>

  @Query("SELECT * FROM FilterEntity WHERE ID = ${FilterEntity.DEFAULT_FILTER_ID}")
  fun getFilter(): FilterEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun storeFilter(filter: FilterEntity)
}