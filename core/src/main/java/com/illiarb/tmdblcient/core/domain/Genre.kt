package com.illiarb.tmdblcient.core.domain

import java.util.Locale

data class Genre(val id: Int, val name: String) {

    companion object {
        const val GENRE_ALL = -1

        const val DRAMA = "drama"
        const val FANTASY = "fantasy"
        const val SCIENCE_FICTION = "science fiction"
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
    @Suppress("ComplexMethod")
    fun getEmoji(): String? {
        return when (name.toLowerCase(Locale.getDefault())) {
            DRAMA -> "\uD83D\uDE28"
            FANTASY -> "\uD83E\uDDD9"
            SCIENCE_FICTION -> "\uD83D\uDE80️"
            ACTION -> "\uD83E\uDD20"
            ADVENTURE -> "\uD83C\uDFDE️"
            CRIME -> "\uD83D\uDC6E"
            THRILLER -> "\uD83D\uDDE1️"
            COMEDY -> "\uD83E\uDD23"
            HORROR -> "\uD83E\uDDDF"
            MYSTERY -> "\uD83D\uDD75️"
            else -> null
        }
    }
}