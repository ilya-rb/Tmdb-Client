package com.illiarb.tmdbexplorer.coreui.widget

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton
import com.illiarb.tmdbexplorer.coreui.R
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

@FlowPreview
class Button @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.style.Theme_Tmdb_Button
) : MaterialButton(context, attrs, defStyleAttr), UiFlowWidget {

    private val uiFlowDelegate: UiFlowWidget = UiFlowWidget.UiFlowDelegate(this)

    override val clicks: Flow<Unit>
        get() = uiFlowDelegate.clicks

    override val coroutineContext: CoroutineContext
        get() = uiFlowDelegate.coroutineContext

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        uiFlowDelegate.onDetachedFromWindow()
    }
}