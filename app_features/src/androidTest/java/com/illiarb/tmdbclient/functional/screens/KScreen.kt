package com.illiarb.tmdbclient.functional.screens

import androidx.annotation.LayoutRes
import com.agoda.kakao.screen.Screen

abstract class KScreen<out T : KScreen<T>> : Screen<T>() {

    @get:LayoutRes
    abstract val layoutId: Int?

    abstract val viewClass: Class<*>
}