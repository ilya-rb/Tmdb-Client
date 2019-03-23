package com.illiarb.tmdblcient.core.domain.entity

import java.io.Serializable

data class Review(val author: String, val content: String, val url: String) : Serializable