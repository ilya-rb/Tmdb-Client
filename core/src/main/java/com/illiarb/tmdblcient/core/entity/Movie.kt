package com.illiarb.tmdblcient.core.entity

import java.io.Serializable

data class Movie(
    val id: Int,
    val adult: Boolean,
    val backdropPath: String?,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String?,
    val imdbId: String?,
    val credits: List<Person>,
    val productionCompanies: List<Company>,
    val releaseDate: String,
    val overview: String?,
    val reviews: List<Review>,
    val runtime: Int?,
    val status: String?,
    val title: String,
    val tagline: String?,
    val images: List<Backdrop>,
    val voteAverage: Float,
    val voteCount: Int
) : Serializable