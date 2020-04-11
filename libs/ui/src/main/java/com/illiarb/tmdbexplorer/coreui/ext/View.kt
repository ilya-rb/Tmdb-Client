package com.illiarb.tmdbexplorer.coreui.ext

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.core.view.ViewCompat
import com.illiarb.tmdbexplorer.coreui.common.SizeSpec

/**
 * @author ilya-rb on 18.10.18.
 */
private const val MARGIN_CURRENT = -1

fun View.setSize(widthSpec: SizeSpec? = null, heightSpec: SizeSpec? = null) {
  val params = layoutParams as ViewGroup.LayoutParams

  widthSpec?.let {
    params.width = when (it) {
      is SizeSpec.MatchParent -> ViewGroup.LayoutParams.MATCH_PARENT
      is SizeSpec.WrapContent -> ViewGroup.LayoutParams.WRAP_CONTENT
      is SizeSpec.Fixed -> resources.getDimensionPixelSize(it.sizeRes)
    }
  }

  heightSpec?.let {
    params.height = when (it) {
      is SizeSpec.MatchParent -> ViewGroup.LayoutParams.MATCH_PARENT
      is SizeSpec.WrapContent -> ViewGroup.LayoutParams.WRAP_CONTENT
      is SizeSpec.Fixed -> resources.getDimensionPixelSize(it.sizeRes)
    }
  }
}

fun View.updatePadding(
  left: Int = paddingLeft,
  top: Int = paddingTop,
  right: Int = paddingRight,
  bottom: Int = paddingBottom
) = setPadding(left, top, right, bottom)

fun View.updateMargin(
  left: Int = MARGIN_CURRENT,
  top: Int = MARGIN_CURRENT,
  right: Int = MARGIN_CURRENT,
  bottom: Int = MARGIN_CURRENT
) {
  val params = layoutParams as ViewGroup.MarginLayoutParams

  if (left != MARGIN_CURRENT) params.leftMargin = left
  if (top != MARGIN_CURRENT) params.topMargin = top
  if (right != MARGIN_CURRENT) params.rightMargin = right
  if (bottom != MARGIN_CURRENT) params.bottomMargin = bottom
}

fun View.setVisible(visible: Boolean) {
  visibility = if (visible) {
    View.VISIBLE
  } else {
    View.GONE
  }
}

fun View.setInvisible(invisible: Boolean) {
  visibility = if (invisible) {
    View.INVISIBLE
  } else {
    View.VISIBLE
  }
}

fun View.getColorAttr(@AttrRes id: Int): Int {
  val outValue = TypedValue()
  context.theme.resolveAttribute(id, outValue, true)
  return outValue.data
}

/**
 * Source:
 * https://android.googlesource.com/platform/frameworks/support/core/ktx/src/main/java/androidx/core/view/View.kt
 * Performs the given action when this view is next laid out.
 *
 * The action will only be invoked once on the next layout and then removed.
 *
 * @see doOnLayout
 */
inline fun View.doOnNextLayout(crossinline action: (view: View) -> Unit) {
  addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
    override fun onLayoutChange(
      view: View,
      left: Int,
      top: Int,
      right: Int,
      bottom: Int,
      oldLeft: Int,
      oldTop: Int,
      oldRight: Int,
      oldBottom: Int
    ) {
      view.removeOnLayoutChangeListener(this)
      action(view)
    }
  })
}

/**
 * Source:
 * https://android.googlesource.com/platform/frameworks/support/core/ktx/src/main/java/androidx/core/view/View.kt
 *
 * Performs the given action when this view is laid out. If the view has been laid out and it
 * has not requested a layout, the action will be performed straight away, otherwise the
 * action will be performed after the view is next laid out.
 *
 * The action will only be invoked once on the next layout and then removed.
 *
 * @see doOnNextLayout
 */
inline fun View.doOnLayout(crossinline action: (view: View) -> Unit) {
  if (ViewCompat.isLaidOut(this) && !isLayoutRequested) {
    action(this)
  } else {
    doOnNextLayout {
      action(it)
    }
  }
}