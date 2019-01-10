package com.illiarb.tmdbclient.feature.search

import android.text.Editable
import android.text.TextWatcher
import java.util.Timer
import java.util.TimerTask

/**
 * @author ilya-rb on 10.01.19.
 */
class DebounceTextWatcher(
    private val onTextChanged: (String) -> Unit
) : TextWatcher {

    companion object {
        // 400 Milliseconds
        const val DEFAULT_DEBOUNCE_TIME = 400L
    }

    private var timer = Timer()

    private var currentText: String = ""

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s == null || s.trim().isEmpty()) return

        val text = s.toString()
        if (currentText == text) {
            return
        }

        timer.cancel()

        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                onTextChanged(text)
                currentText = text
            }
        }, DEFAULT_DEBOUNCE_TIME)
    }
}