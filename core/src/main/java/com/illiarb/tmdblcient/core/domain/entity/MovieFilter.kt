package com.illiarb.tmdblcient.core.domain.entity

/**
 * @author ilya-rb on 31.10.18.
 */
data class MovieFilter(val name: String, val code: String) {

    companion object {
        const val TYPE_POPULAR = "popular"
        const val TYPE_NOW_PLAYING = "now_playing"
        const val TYPE_UPCOMING = "upcoming"
        const val TYPE_TOP_RATED = "top_rated"
    }
}