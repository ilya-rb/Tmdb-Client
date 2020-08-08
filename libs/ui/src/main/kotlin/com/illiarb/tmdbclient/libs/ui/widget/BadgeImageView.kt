package com.illiarb.tmdbclient.libs.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import com.illiarb.tmdbclient.libs.ui.R
import com.illiarb.tmdbclient.libs.ui.databinding.WidgetBadgeImageViewBinding
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

    context.withStyledAttributes(
      set = attrs,
      attrs = R.styleable.BadgeImageView,
      defStyleAttr = defStyleAttr
    ) {
      getDrawable(R.styleable.BadgeImageView_bivIcon)?.let {
        val iconTint = getColor(R.styleable.BadgeImageView_bivIconTint, 0)
        if (iconTint == 0) {
          binding.bivImage.setImageDrawable(it)
        } else {
          binding.bivImage.setImageDrawable(it.mutate().apply { setTint(iconTint) })
        }
      }
    }
  }

  fun setBadgeVisible(visible: Boolean) {
    binding.bivBadge.setVisible(visible)
  }

  fun setBadgeCount(count: Int) {
    binding.bivBadge.text = count.toString()
  }
}