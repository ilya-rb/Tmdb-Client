package com.illiarb.tmdbexplorer.coreui.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : AppCompatActivity() {

    protected val destroyDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val contentView = getContentView()
        if (contentView != View.NO_ID) {
            setContentView(contentView)
        }
    }

    override fun onDestroy() {
        destroyDisposable.clear()
        super.onDestroy()
    }

    @LayoutRes
    protected abstract fun getContentView(): Int
}