package com.illiarb.tmdblcient.core.storage

import java.util.Locale

interface ResourceResolver {

    fun getString(stringResId: Int): String

    fun getStringArray(arrayResId: Int): Array<String>

    fun getUserLocale(): Locale

    fun getUserISOCountry(): String

}