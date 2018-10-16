package com.illiarb.tmdbclient.network.responses

data class ResultsResponse <T> (val results: List<T>)