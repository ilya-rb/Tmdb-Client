package com.illiarb.tmdbclient.libs.ui.widget.recyclerview.delegates

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.illiarb.tmdbclient.libs.ui.databinding.ItemProgressBinding

fun progressDelegate() = adapterDelegateViewBinding<Any, Any, ItemProgressBinding>(
  { inflater, root -> ItemProgressBinding.inflate(inflater, root, false) }
) {
  // No-op
}

/**
 * Source:
 * https://gitlab.com/terrakok/gitlab-client/-/blob/develop/app/src/main/java/ru/terrakok/gitlabclient/ui/global/list/ProgressItem.kt
 *
 * @author Konstantin Tskhovrebov (aka terrakok) on 18.06.17.
 */
object ProgressItem