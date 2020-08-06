package com.illiarb.tmdbclient.services.tmdb.di.internal

import android.app.Application
import androidx.room.Room
import com.illiarb.tmdbclient.services.tmdb.internal.db.TmdbDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal object DatabaseModule {

  @Provides
  @JvmStatic
  @Singleton
  fun provideTmdbDatabase(app: Application): TmdbDatabase {
    return Room.databaseBuilder(app, TmdbDatabase::class.java, TmdbDatabase.DATABASE_NAME)
      .build()
  }
}