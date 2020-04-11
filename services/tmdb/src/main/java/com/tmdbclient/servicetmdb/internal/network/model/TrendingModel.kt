package com.tmdbclient.servicetmdb.internal.network.model

internal interface TrendingModel {

  /**
   * Fallback for unknown models
   */
  object Stub : TrendingModel
}