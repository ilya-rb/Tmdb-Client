package com.illiarb.tmdblcient.core.domain

import com.illiarb.tmdblcient.core.util.Option

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
    val videos: List<Video>? = null
) {

    companion object {
        const val DELIMITER_SLASH_SPACED = " / "
    }

    fun getGenresString(delimiter: String = DELIMITER_SLASH_SPACED): Option<String> {
        return if (genres.isNotEmpty()) {
            val result = buildString {
                genres.forEachIndexed { index, genre ->
                    append(genre.name)
                    append(" ")

                    val emoji = genre.getEmoji()
                    emoji.invokeIfSome {
                        append(it)
                    }

                    if (index < genres.size - 1) {
                        append(delimiter)
                    }
                }
            }
            Option.Some(result)
        } else {
            Option.None()
        }
    }
}