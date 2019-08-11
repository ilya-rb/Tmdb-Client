package com.illiarb.tmdbexplorer.coreui.widget

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton
import com.illiarb.tmdbexplorer.coreui.R

class Button @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.style.Theme_Tmdb_Button
) : MaterialButton(context, attrs, defStyleAttr)