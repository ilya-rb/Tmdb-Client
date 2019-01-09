package com.illiarb.tmdbclient.feature.home.list

import com.illiarb.tmdbexplorer.coreui.base.BaseViewModel
import com.illiarb.tmdblcient.core.domain.auth.Authenticator
import com.illiarb.tmdblcient.core.domain.movie.GetAllMovies
import com.illiarb.tmdblcient.core.entity.ListSection
import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.entity.NowPlayingSection
import com.illiarb.tmdblcient.core.navigation.AccountScreen
import com.illiarb.tmdblcient.core.navigation.AuthScreen
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.navigation.SearchScreen
import com.illiarb.tmdblcient.core.system.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
class HomeModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getAllMovies: GetAllMovies,
    private val authenticator: Authenticator,
    private val router: Router
) : BaseViewModel() {

    private val stateObservable = SimpleStateObservable<HomeState>()

    init {
        stateObservable.accept(HomeState.idle())

        launch(context = coroutineContext) {
            val movies = getAllMovies()
                .map { (filter, movies) ->
                    if (filter.code == MovieFilter.TYPE_NOW_PLAYING) {
                        NowPlayingSection(filter.name, movies)
                    } else {
                        ListSection(filter.name, movies)
                    }
                }

            stateObservable.accept(HomeState(false, movies))
        }
    }

    override fun provideDefaultDispatcher(): CoroutineDispatcher = dispatcherProvider.mainDispatcher

    fun getStateObservable(): StateObservable<HomeState> = stateObservable

    fun onSearchClick() = router.navigateTo(SearchScreen)

    fun onAccountClick() {
        launch {
            if (authenticator.isAuthenticated()) {
                router.navigateTo(AccountScreen)
            } else {
                router.navigateTo(AuthScreen)
            }
        }
    }
}