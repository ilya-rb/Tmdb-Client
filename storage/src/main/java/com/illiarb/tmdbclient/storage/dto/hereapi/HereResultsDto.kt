package com.illiarb.tmdbclient.storage.dto.hereapi

/**
 * @author ilya-rb on 02.11.18.
 */
data class HereResults(val results: HereLocationList)

data class HereLocationList(val next: String, val items: List<HereLocationDto>)