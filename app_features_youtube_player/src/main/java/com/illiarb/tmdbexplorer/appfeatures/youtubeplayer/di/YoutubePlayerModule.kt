package com.illiarb.tmdbexplorer.appfeatures.youtubeplayer.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbexplorer.appfeatures.youtubeplayer.DefaultYoutubePlayerModel
import com.illiarb.tmdbexplorer.coreui.di.ViewModelKey
import com.illiarb.tmdblcient.core.interactor.MoviesInteractor
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class YoutubePlayerModule(private val movieId: Int) {

    @Provides
    @IntoMap
    @ViewModelKey(DefaultYoutubePlayerModel::class)
    fun provideYoutubePlayerModel(interactor: MoviesInteractor): ViewModel {
        return DefaultYoutubePlayerModel(movieId, interactor)
    }
}