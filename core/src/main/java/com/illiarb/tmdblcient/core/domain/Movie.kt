package com.illiarb.tmdblcient.core.domain

data class Movie(
    val id: Int,
    val posterPath: Image?,
    val backdropPath: Image?,
    val genres: List<Genre>,
    val homepage: String?,
    val credits: List<Person>,
    val releaseDate: String,
    val overview: String?,
    val reviews: List<Review>,
    val runtime: Int,
    val title: String,
    val images: List<Image>,
    val voteAverage: Float,
    val country: String?,
    val rating: Int = 0,
    val video: Boolean
) {

    companion object {
        const val DELIMITER_SLASH_SPACED = " / "
    }

    fun getGenresString(delimiter: String = DELIMITER_SLASH_SPACED): String? {
        if (genres.isNotEmpty()) {
            return buildString {
                genres.forEachIndexed { index, genre ->
                    append(genre.name)

                    if (index < genres.size - 1) {
                        append(delimiter)
                    }
                }
            }
        }
        return null
    }
}