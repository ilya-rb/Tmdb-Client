package com.illiarb.tmdblcient.core.navigation

import com.illiarb.tmdblcient.core.domain.Genre

/**
 * @author ilya-rb on 18.11.18.
 */
interface Router {

    fun executeAction(action: Action)

    sealed class Action {

        class ShowMovieDetails(val id: Int) : Action() {
            companion object {
                const val EXTRA_MOVIE_DETAILS = "id"
            }
        }

        class ShowDiscover(val id: Int = Genre.GENRE_ALL) : Action() {
            companion object {
                const val EXTRA_GENRE_ID = "id"
            }
        }

        object ShowSettings : Action()
    }
}