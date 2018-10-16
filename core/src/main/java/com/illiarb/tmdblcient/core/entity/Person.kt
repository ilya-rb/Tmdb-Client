package com.illiarb.tmdblcient.core.entity

import java.io.Serializable

data class Person(
    val id: Int,
    val name: String,
    val character: String,
    val profilePath: String
) : Serializable