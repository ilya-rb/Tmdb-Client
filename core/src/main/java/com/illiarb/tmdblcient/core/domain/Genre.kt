package com.illiarb.tmdblcient.core.domain

data class Genre(val id: Int, val name: String) {

    companion object {
        const val GENRE_ALL = -1
    }
}