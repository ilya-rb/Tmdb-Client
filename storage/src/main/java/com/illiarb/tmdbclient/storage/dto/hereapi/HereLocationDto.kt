package com.illiarb.tmdbclient.storage.dto.hereapi

/**
 * @author ilya-rb on 02.11.18.
 */
data class HereLocationDto(
    val id: String,
    val position: List<Double>,
    val distance: Int,
    val title: String
)