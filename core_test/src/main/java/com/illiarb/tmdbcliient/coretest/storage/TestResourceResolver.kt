package com.illiarb.tmdbcliient.coretest.storage

import com.illiarb.tmdblcient.core.storage.ResourceResolver
import java.util.Locale

class TestResourceResolver : ResourceResolver {

    override fun getString(stringResId: Int): String = ""

    override fun getStringArray(arrayResId: Int): Array<String> = arrayOf()

    override fun getUserLocale(): Locale = Locale.getDefault()

    override fun getUserISOCountry(): String = ""
}