package com.illiarb.tmdblcient.core.domain

import java.util.*

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