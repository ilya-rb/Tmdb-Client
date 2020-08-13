package com.illiarb.tmdbclient.libs.ui.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.widget.AppCompatTextView

class ExpandableTextView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

  companion object {
    const val ANIMATION_PROPERTY = "height"
    const val DEFAULT_MAX_LINES = 3
    const val DEFAULT_ANIMATION_DURATION = 500L
  }

  private val defaultInterpolator = AccelerateDecelerateInterpolator()

  private val expandAnimator by lazy {
    ObjectAnimator.ofInt(this, ANIMATION_PROPERTY, originalHeight)
  }

  private val collapseAnimator by lazy {
    ObjectAnimator.ofInt(this, ANIMATION_PROPERTY, collapsedHeight)
  }

  private var isExpanded = false
  private var expandListener: ExpandListener? = null
  private var textMaxLines = DEFAULT_MAX_LINES
  private var saveBaseState = false

  private var collapsedHeight = 0
  private var originalHeight = 0

  override fun onSaveInstanceState(): Parcelable? {
    return if (saveBaseState) {
      super.onSaveInstanceState()?.let {
        SavedState(it, originalHeight, collapsedHeight, isExpanded)
      }
    } else {
      super.onSaveInstanceState()
    }
  }

  override fun onRestoreInstanceState(state: Parcelable?) {
    if (state !is SavedState) {
      super.onRestoreInstanceState(state)
      return
    }

    super.onRestoreInstanceState(state.superState)

    originalHeight = state.originalHeight
    collapsedHeight = state.collapsedHeight

    if (state.isExpanded) {
      collapse(false)
    } else {
      expand(false)
    }
  }

  fun expand(runAnimation: Boolean = true) {
    if (runAnimation) {
      expandAnimator.duration = DEFAULT_ANIMATION_DURATION
      expandAnimator.interpolator = defaultInterpolator

      expandAnimator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
          super.onAnimationStart(animation)
          isExpanded = true
          expandListener?.onExpanded()
        }
      })

      expandAnimator.start()
    } else {
      isExpanded = true
      height = originalHeight
      expandListener?.onExpanded()
    }
  }

  fun collapse(runAnimation: Boolean = true) {
    if (runAnimation) {
      collapseAnimator.duration = DEFAULT_ANIMATION_DURATION
      collapseAnimator.interpolator = defaultInterpolator

      collapseAnimator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
          super.onAnimationStart(animation)
          isExpanded = false
          expandListener?.onCollapsed()
        }
      })

      collapseAnimator.start()
    } else {
      isExpanded = false
      height = collapsedHeight
      expandListener?.onCollapsed()
    }
  }

  fun toggle(runAnimation: Boolean = true) {
    if (isExpanded) collapse(runAnimation) else expand(runAnimation)
  }

  fun setExpandListener(expandListener: ExpandListener) {
    this.expandListener = expandListener
  }

  fun setSaveBaseState(save: Boolean) {
    saveBaseState = save
  }

  fun isExpanded(): Boolean {
    return isExpanded
  }

  fun setTextMaxLines(count: Int): Boolean {
    textMaxLines = count

    // Measure expanded height
    measure(
      MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY),
      MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
    )
    originalHeight = measuredHeight

    // Change max lines
    // and measure collapsed height
    maxLines = count
    measure(
      MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY),
      MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
    )
    collapsedHeight = measuredHeight

    return originalHeight > collapsedHeight
  }

  class SavedState : BaseSavedState {

    val originalHeight: Int
    val collapsedHeight: Int
    val isExpanded: Boolean

    constructor(
      superState: Parcelable,
      originalHeight: Int,
      collapsedHeight: Int,
      isExpanded: Boolean
    ) : super(superState) {

      this.originalHeight = originalHeight
      this.collapsedHeight = collapsedHeight
      this.isExpanded = isExpanded
    }

    private constructor(parcel: Parcel) : super(parcel) {
      originalHeight = parcel.readInt()
      collapsedHeight = parcel.readInt()
      isExpanded = parcel.readInt() == 1
    }

    override fun writeToParcel(out: Parcel?, flags: Int) {
      super.writeToParcel(out, flags)
      out?.writeInt(originalHeight)
      out?.writeInt(collapsedHeight)
      out?.writeInt(if (isExpanded) 1 else 0)
    }

    companion object {

      @JvmStatic
      @Suppress("unused")
      val CREATOR = object : Parcelable.Creator<SavedState> {
        override fun createFromParcel(source: Parcel): SavedState = SavedState(source)
        override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
      }
    }
  }

  interface ExpandListener {

    fun onExpanded()

    fun onCollapsed()
  }
}