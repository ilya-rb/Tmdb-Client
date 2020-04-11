package com.tmdbclient.servicetmdb.domain

import java.io.Serializable

data class Review(val author: String, val content: String, val url: String) : Serializable