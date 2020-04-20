package com.illiarb.tmdbclient.libs.ui.widget.recyclerview.pagination

import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.delegates.ProgressItem
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.delegates.progressDelegate
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.simpleDiffUtilCallback

/**
 * Adapter from here:
 * https://gitlab.com/terrakok/gitlab-client/-/blob/develop/app/src/main/java/ru/terrakok/gitlabclient/ui/global/list/PaginalAdapter.kt
 */
class PaginalAdapter(
  vararg delegates: AdapterDelegate<List<Any>>,
  itemDiff: (old: Any, new: Any) -> Boolean = { old, new -> old == new }
) : AsyncListDifferDelegationAdapter<Any>(simpleDiffUtilCallback(itemDiff)) {

  init {
    items = mutableListOf()

    delegates.forEach {
      delegatesManager.addDelegate(it)
    }

    delegatesManager.addDelegate(progressDelegate())
  }

  fun update(data: List<Any>, isPageProgress: Boolean) {
    items = mutableListOf<Any>().apply {
      addAll(data)

      if (isPageProgress) {
        add(ProgressItem)
      }
    }
  }
}
