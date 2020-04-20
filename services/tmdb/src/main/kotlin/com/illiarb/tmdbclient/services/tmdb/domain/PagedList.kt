package com.illiarb.tmdbclient.services.tmdb.domain

data class PagedList<T>(
  val items: List<T>,
  val page: Int,
  val totalPages: Int
)