package com.illiarb.tmdblcient.core.system

interface ResourceResolver {

    fun getString(stringResId: Int): String

    fun getStringArray(arrayResId: Int): Array<String>

}