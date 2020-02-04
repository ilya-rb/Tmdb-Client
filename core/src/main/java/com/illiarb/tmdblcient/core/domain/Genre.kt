package com.illiarb.tmdblcient.core.domain

data class Genre(val id: Int, val name: String) {

    companion object {
        const val GENRE_ALL = -1

        const val DRAMA = "drama"
        const val FANTASY = "fantasy"
        const val SCIENCE_FICTION = "science-fiction"
        const val ACTION = "action"
        const val ADVENTURE = "adventure"
        const val CRIME = "crime"
        const val THRILLER = "thriller"
        const val COMEDY = "comedy"
        const val HORROR = "horror"
        const val MYSTERY = "mystery"
    }

    /**
     * Source:
     * https://github.com/chrisbanes/tivi/blob/master/data/src/main/java/app/tivi/data/entities/Genre.kt
     */
    fun getEmoji(): String = when (name) {
        Genre.DRAMA -> "\uD83D\uDE28"
        Genre.FANTASY -> "\uD83E\uDDD9"
        Genre.SCIENCE_FICTION -> "\uD83D\uDE80️"
        Genre.ACTION -> "\uD83E\uDD20"
        Genre.ADVENTURE -> "\uD83C\uDFDE️"
        Genre.CRIME -> "\uD83D\uDC6E"
        Genre.THRILLER -> "\uD83D\uDDE1️"
        Genre.COMEDY -> "\uD83E\uDD23"
        Genre.HORROR -> "\uD83E\uDDDF"
        Genre.MYSTERY -> "\uD83D\uDD75️"
        else -> ""
    }
}