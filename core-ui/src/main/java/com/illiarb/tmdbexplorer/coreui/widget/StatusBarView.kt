package com.illiarb.tmdbexplorer.coreui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.illiarb.tmdbexplorer.coreui.ext.getStatusBarHeight

/**
 * @author ilya-rb on 06.11.18.
 */
class StatusBarView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), context.getStatusBarHeight())
    }
}