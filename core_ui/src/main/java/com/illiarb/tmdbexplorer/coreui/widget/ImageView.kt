package com.illiarb.tmdbexplorer.coreui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlin.coroutines.CoroutineContext

@FlowPreview
class ImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr), UiFlowWidget {

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