package com.illiarb.tmdbexplorer.coreui.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class EditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private val viewScopeDelegate = CoroutineViewScopeDelegate()

    private val textChanges = Channel<CharSequence>()

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewScopeDelegate.onDetachedFromWindow()
    }

    fun textChanged(): Flow<CharSequence> {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewScopeDelegate.launch {
                    text?.let {
                        textChanges.send(it)
                    }
                }
            }
        })
        return textChanges.consumeAsFlow()
    }
}