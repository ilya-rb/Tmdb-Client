package com.illiarb.tmdblcient.core.navigation

/**
 * @author ilya-rb on 18.11.18.
 */
interface Router {

    fun executeAction(action: Action)

    sealed class Action {

        data class ShowMovieDetails(val id: Int) : Action() {
            companion object {
                const val EXTRA_MOVIE_DETAILS = "id"
            }
        }

        object ShowDiscover : Action()
    }
}