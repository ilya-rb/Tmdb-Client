package com.illiarb.tmdbclient.storage.dto

data class MovieDto(
    val id: Int,
    val adult: Boolean,
    val posterPath: String?,
    val budget: Int,
    val genres: List<GenreDto>,
    val homepage: String?,
    val imdbId: String?,
    val credits: CreditsDto?,
    val productionCompanies: List<CompanyDto>?,
    val releaseDate: String,
    val overview: String?,
    val reviews: ReviewListDto?,
    val runtime: Int?,
    val status: String?,
    val title: String,
    val tagline: String?,
    val images: BackdropListDto?,
    val voteAverage: Float,
    val voteCount: Int
)