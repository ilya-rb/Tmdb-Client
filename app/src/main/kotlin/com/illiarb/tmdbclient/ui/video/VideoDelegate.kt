package com.illiarb.tmdbclient.ui.video

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.illiarb.tmdbclient.databinding.ItemVideoBinding
import com.illiarb.tmdbclient.libs.ui.common.OnClickListener
import com.illiarb.tmdbclient.libs.ui.ext.setVisible
import com.illiarb.tmdbclient.ui.video.VideoListViewModel.UiVideo

fun videoDelegate(
  clickListener: OnClickListener<UiVideo>
) = adapterDelegateViewBinding<UiVideo, Any, ItemVideoBinding>(
  { inflater, root -> ItemVideoBinding.inflate(inflater, root, false) }
) {

  bind {
    binding.itemVideoTitle.text = item.video.name
    binding.itemVideoSelected.setVisible(item.isSelected)

    itemView.setOnClickListener {
      clickListener(item)
    }
  }
}