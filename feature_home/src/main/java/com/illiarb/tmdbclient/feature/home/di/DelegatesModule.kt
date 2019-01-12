package com.illiarb.tmdbclient.feature.home.di

import com.illiarb.tmdbclient.feature.home.list.ui.delegates.movie.MovieSectionDelegate
import com.illiarb.tmdbclient.feature.home.list.ui.delegates.nowplaying.NowPlayingSectionDelegate
import com.illiarb.tmdbexplorer.coreui.recyclerview.adapter.AdapterDelegate
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

/**
 * @author ilya-rb on 05.11.18.
 */
@Module
interface DelegatesModule {

    @Binds
    @IntoSet
    fun bindMovieSectionDelegate(delegate: MovieSectionDelegate): AdapterDelegate

    @Binds
    @IntoSet
    fun bindNowPlayingSectionDelegate(delegate: NowPlayingSectionDelegate): AdapterDelegate
}