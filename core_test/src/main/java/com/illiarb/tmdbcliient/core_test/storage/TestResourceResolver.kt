package com.illiarb.tmdbcliient.core_test.storage

import com.illiarb.tmdblcient.core.storage.ResourceResolver

class TestResourceResolver : ResourceResolver {

    override fun getString(stringResId: Int): String = ""

    override fun getStringArray(arrayResId: Int): Array<String> = arrayOf()
}