package com.illiarb.tmdbclient.libs.test.tools

import com.illiarb.tmdbclient.libs.tools.ResourceResolver
import java.util.Locale

class TestResourceResolver : ResourceResolver {

  override fun getString(stringResId: Int): String = ""

  override fun getStringArray(arrayResId: Int): Array<String> = arrayOf()

  override fun getUserLocale(): Locale = Locale.getDefault()

  override fun getUserISOCountry(): String = ""
}