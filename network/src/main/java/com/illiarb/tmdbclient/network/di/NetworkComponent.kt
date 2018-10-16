package com.illiarb.tmdbclient.network.di

import com.illiarb.tmdbexplorerdi.providers.NetworkProvider
import dagger.Component
import javax.inject.Singleton

@Component(modules = [ClientsModule::class, NetworkModule::class])
@Singleton
interface NetworkComponent : NetworkProvider {
    companion object {
        fun get(): NetworkProvider = DaggerNetworkComponent.create()
    }
}