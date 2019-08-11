package com.illiarb.tmdbexplorer.coreui.widget

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textview.MaterialTextView
import com.illiarb.tmdbexplorer.coreui.R

class TextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.style.Theme_Tmdb_TextView
) : MaterialTextView(context, attrs, defStyleAttr)