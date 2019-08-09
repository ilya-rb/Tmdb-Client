package com.illiarb.tmdbexplorer.coreui.widget

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textview.MaterialTextView
import com.illiarb.tmdbexplorer.coreui.R
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

@FlowPreview
class TextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.style.Theme_Tmdb_TextView
) : MaterialTextView(context, attrs, defStyleAttr), UiFlowWidget {

    private val flowDelegate = UiFlowWidget.UiFlowDelegate(this)

    override val clicks: Flow<Unit>
        get() = flowDelegate.clicks

    override val coroutineContext: CoroutineContext
        get() = flowDelegate.coroutineContext

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        flowDelegate.onDetachedFromWindow()
    }
}