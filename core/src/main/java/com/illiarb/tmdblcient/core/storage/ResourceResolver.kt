package com.illiarb.tmdblcient.core.storage

interface ResourceResolver {

    fun getString(stringResId: Int): String

    fun getStringArray(arrayResId: Int): Array<String>

}