package com.illiarb.tmdbclient.resources

import android.content.Context
import com.illiarb.tmdblcient.core.resources.ResourceResolver

class AndroidResourceResolver constructor(private val context: Context) : ResourceResolver {

    override fun getString(stringResId: Int): String = context.getString(stringResId)
}