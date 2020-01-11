package com.illiarb.tmdblcient.core.domain

data class Video(val key: String, val site: String) {

    companion object {
        const val SITE_YOUTUBE = "YouTube"
    }
}