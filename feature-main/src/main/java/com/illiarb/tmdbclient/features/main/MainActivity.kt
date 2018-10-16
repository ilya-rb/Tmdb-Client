package com.illiarb.tmdbclient.features.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.illiarb.tmdbclient.features.main.di.MainComponent
import com.illiarb.tmdbexplorer.coreui.base.BaseActivity
import com.illiarb.tmdbexplorerdi.Injectable
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import javax.inject.Inject

class MainActivity : BaseActivity(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelFactory.create(MainViewModel::class.java)

        if (savedInstanceState == null) {
            viewModel.onScreenInitialized()
        }
    }

    override fun getContentView(): Int = R.layout.activity_main

    override fun inject(appProvider: AppProvider) {
        MainComponent.get(appProvider, this).inject(this)
    }
}