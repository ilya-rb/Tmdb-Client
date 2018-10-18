package com.illiarb.tmdbclient.features.main

import androidx.navigation.Navigation
import com.illiarb.tmdbclient.features.main.di.MainComponent
import com.illiarb.tmdbexplorer.coreui.base.BaseActivity
import com.illiarb.tmdbexplorerdi.Injectable
import com.illiarb.tmdbexplorerdi.providers.AppProvider

class MainActivity : BaseActivity<MainViewModel>(), Injectable {

    override fun getContentView(): Int = R.layout.activity_main

    override fun getViewModelClass(): Class<MainViewModel> = MainViewModel::class.java

    override fun inject(appProvider: AppProvider) =
        MainComponent.get(appProvider, this).inject(this)

    override fun onNavigateUp(): Boolean =
        Navigation
            .findNavController(this, R.id.nav_host_fragment)
            .navigateUp()
}