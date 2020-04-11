package com.illiarb.tmdbclient.services.tmdb.internal.network.model

internal interface TrendingModel {

  /**
   * Fallback for unknown models
   */
  object Stub : TrendingModel
}