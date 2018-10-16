package com.illiarb.tmdbclient.network.responses

data class MovieResponse(
    val id: Int,
    val adult: Boolean,
    val backdropPath: String?,
    val budget: Int,
    val genres: List<GenreResponse>,
    val homepage: String?,
    val imdbId: String?,
    val credits: CreditsResponse?,
    val productionCompanies: List<CompanyResponse>?,
    val releaseDate: String,
    val overview: String?,
    val reviews: ReviewListResponse?,
    val runtime: Int?,
    val status: String?,
    val title: String,
    val tagline: String?,
    val images: BackdropListResponse?,
    val voteAverage: Float,
    val voteCount: Int
)