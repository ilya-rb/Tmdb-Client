package com.illiarb.core_ui_recycler_view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewStub
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates

class RecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var state: State by Delegates.observable(State.Empty as State) { _, old, new ->
        if (old != new) {
            showState(new)
        }
    }

    private val emptyViewStub: ViewStub
    private val errorViewStub: ViewStub
    private val recyclerView: RecyclerView
    private val progressBar: View

    init {
        val view = inflate(context, R.layout.widget_recycler_view, this)

        emptyViewStub = view.findViewById(R.id.viewStubEmptyView)
        errorViewStub = view.findViewById(R.id.viewStubErrorView)
        recyclerView = view.findViewById(R.id.recyclerView)
        progressBar = view.findViewById(R.id.progressBar)
    }

    fun recyclerView(): RecyclerView = recyclerView

    fun withRecyclerView(block: RecyclerView.() -> Unit) {
        block(recyclerView)
    }

    fun moveToState(newState: State) {
        state = newState
    }

    fun setEmptyView(@LayoutRes resId: Int) {
        emptyViewStub.layoutResource = resId
        emptyViewStub.inflate()
    }

    fun setErrorView(@LayoutRes resId: Int) {
        errorViewStub.layoutResource = resId
        errorViewStub.inflate()
    }

    private fun showState(state: State) {
        emptyViewStub.visibility = if (state is State.Empty) View.VISIBLE else View.GONE
        recyclerView.visibility = if (state is State.Content) View.VISIBLE else View.GONE
        errorViewStub.visibility = if (state is State.Error) View.VISIBLE else View.GONE
        progressBar.visibility = if (state is State.Loading) View.VISIBLE else View.GONE
    }

    sealed class State {
        object Empty : State()
        object Loading : State()
        object Content : State()
        object Error : State()
    }
}