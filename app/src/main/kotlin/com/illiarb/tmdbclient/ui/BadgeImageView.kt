package com.illiarb.tmdbclient.ui

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.FrameLayout
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.databinding.WidgetBadgeImageViewBinding
import com.illiarb.tmdbclient.libs.ui.ext.setVisible

class BadgeImageView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

  private val binding: WidgetBadgeImageViewBinding

  init {
    inflate(context, R.layout.widget_badge_image_view, this).also {
      binding = WidgetBadgeImageViewBinding.bind(it)
    }

    var attributes: TypedArray? = null
    try {
      attributes = context.obtainStyledAttributes(attrs, R.styleable.BadgeImageView)
      attributes.getDrawable(R.styleable.BadgeImageView_bivIcon)?.let {
        val iconTint = attributes.getColor(R.styleable.BadgeImageView_bivIconTint, 0)
        if (iconTint == 0) {
          binding.bivImage.setImageDrawable(it)
        } else {
          binding.bivImage.setImageDrawable(it.mutate().apply { setTint(iconTint) })
        }
      }
    } finally {
      attributes?.recycle()
    }
  }

  fun setBadgeVisible(visible: Boolean) {
    binding.bivBadge.setVisible(visible)
  }

  fun setBadgeCount(count: Int) {
    binding.bivBadge.text = count.toString()
  }
}