package com.tmdbclient.servicetmdb.model

interface TrendingModel {

  /**
   * Fallback for unknown models
   */
  object Stub : TrendingModel
}