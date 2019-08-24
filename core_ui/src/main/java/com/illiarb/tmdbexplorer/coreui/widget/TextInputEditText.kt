package com.illiarb.tmdbexplorer.coreui.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.illiarb.tmdbexplorer.coreui.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class TextInputEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.editTextStyle
) : TextInputEditText(context, attrs, defStyleAttr) {

    fun focusChanges(): Flow<Boolean> = callbackFlow {
        setOnFocusChangeListener { _, hasFocus ->
            offer(hasFocus)
        }
        awaitClose()
    }

    fun textChanges(): Flow<CharSequence> = callbackFlow {
        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                offer(s ?: "")
            }
        }

        addTextChangedListener(watcher)

        awaitClose {
            removeTextChangedListener(watcher)
        }
    }
}