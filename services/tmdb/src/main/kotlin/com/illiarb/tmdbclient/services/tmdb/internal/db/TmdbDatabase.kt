package com.illiarb.tmdbclient.services.tmdb.internal.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.illiarb.tmdbclient.services.tmdb.internal.db.entity.FilterEntity

@Database(entities = [FilterEntity::class], version = 1)
@TypeConverters(Converters::class)
internal abstract class TmdbDatabase : RoomDatabase() {

  companion object {
    const val DATABASE_NAME = "tmdb_database"
  }

  abstract fun filterDao(): FilterDao
}