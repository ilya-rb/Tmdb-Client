package com.illiarb.tmdbexplorer.appfeatures.youtubeplayer.di

import com.illiarb.tmdbexplorer.appfeatures.youtubeplayer.YoutubePlayerFragment
import com.illiarb.tmdbexplorer.coreui.di.ViewModelModule
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import dagger.Component

@Component(
    dependencies = [AppProvider::class],
    modules = [
        ViewModelModule::class,
        YoutubePlayerModule::class
    ]
)
interface YoutubePlayerComponent {

    companion object {

        fun get(appProvider: AppProvider, movieId: Int): YoutubePlayerComponent {
            return DaggerYoutubePlayerComponent.builder()
                .appProvider(appProvider)
                .youtubePlayerModule(YoutubePlayerModule(movieId))
                .build()
        }
    }

    fun inject(fragment: YoutubePlayerFragment)
}