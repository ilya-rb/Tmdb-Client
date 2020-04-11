package com.illiarb.tmdbclient.libs.tools.internal

import android.app.Application
import com.illiarb.tmdbclient.libs.tools.ResourceResolver
import java.util.Locale
import javax.inject.Inject

/**
 * @author ilya-rb on 31.10.18.
 */
internal class AndroidResourceResolver @Inject constructor(app: Application) : ResourceResolver {

  private val resources = app.resources

  override fun getString(stringResId: Int): String = resources.getString(stringResId)

  override fun getStringArray(arrayResId: Int): Array<String> = resources.getStringArray(arrayResId)

  override fun getUserLocale(): Locale = resources.configuration.locale

  override fun getUserISOCountry(): String = resources.configuration.locale.isO3Country
}