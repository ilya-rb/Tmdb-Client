package com.illiarb.tmdbclient.modules.video

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.illiarb.tmdbclient.databinding.ItemVideoTypeBinding
import com.illiarb.tmdbclient.modules.video.VideoListViewModel.UiVideoSection

fun videoTypeDelegate() = adapterDelegateViewBinding<UiVideoSection, Any, ItemVideoTypeBinding>(
  { inflater, root -> ItemVideoTypeBinding.inflate(inflater, root, false) }
) {

  bind {
    binding.itemVideoType.text = item.title
    binding.itemVideoCount.text = item.count.toString()
  }
}