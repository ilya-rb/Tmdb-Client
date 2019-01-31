package com.illiarb.tmdblcient.core.domain.entity

data class Movie(
    val id: Int,
    val posterPath: String?,
    val backdropPath: String?,
    val genres: List<Genre>,
    val homepage: String?,
    val credits: List<Person>,
    val releaseDate: String,
    val overview: String?,
    val reviews: List<Review>,
    val runtime: Int,
    val title: String,
    val images: List<String>,
    val voteAverage: Float,
    val rating: Int = 0
)