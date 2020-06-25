package com.illiarb.tmdbclient.services.tmdb.di

import android.app.Application
import androidx.room.Room
import com.illiarb.tmdbclient.services.tmdb.internal.db.TmdbDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {

  @Provides
  @JvmStatic
  @Singleton
  internal fun provideTmdbDatabase(app: Application): TmdbDatabase {
    return Room.databaseBuilder(app, TmdbDatabase::class.java, TmdbDatabase.DATABASE_NAME)
      .build()
  }
}