package com.illiarb.tmdbclient.storage.local

import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.system.ResourceResolver

/**
 * @author ilya-rb on 31.10.18.
 */
class AndroidResourceResolver(app: App) : ResourceResolver {

    private val resources = app.getApplication().resources

    override fun getString(stringResId: Int): String = resources.getString(stringResId)

    override fun getStringArray(arrayResId: Int): Array<String> = resources.getStringArray(arrayResId)
}