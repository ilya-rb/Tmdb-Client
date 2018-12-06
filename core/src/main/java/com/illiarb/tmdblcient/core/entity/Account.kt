package com.illiarb.tmdblcient.core.entity

import java.util.Collections

/**
 * @author ilya-rb on 20.11.18.
 */
data class Account(
    val id: Int,
    val name: String,
    val username: String,
    val avatar: String,
    val averageRating: Int = 0,
    val favoriteMovies: List<Movie> = Collections.emptyList()
)