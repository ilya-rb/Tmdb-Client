package com.illiarb.tmdbclient.dynamic.feature.account.profile.presentation

import com.illiarb.tmdbclient.dynamic.feature.account.profile.domain.GetProfileUseCase
import com.illiarb.tmdbclient.dynamic.feature.account.profile.domain.SignOutUseCase
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.navigation.AuthScreen
import com.illiarb.tmdblcient.core.navigation.MovieDetailsScreen
import com.illiarb.tmdblcient.core.navigation.Router
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * @author ilya-rb on 07.01.19.
 */
@ExperimentalCoroutinesApi
class AccountModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val router: Router
) : BasePresentationModel<AccountUiState>(AccountUiState.idle()), CoroutineScope {

    init {
        runCoroutine {
            handleResult(getProfileUseCase.executeAsync(Unit), { account ->
                setState { copy(isLoading = false, account = account) }
            })
        }
    }

    fun onFavoriteMovieClick(movie: Movie) {
        router.navigateTo(MovieDetailsScreen(movie.id))
    }

    fun onSignOutConfirm() {
        runCoroutine {
            handleResult(signOutUseCase.executeAsync(Unit), { router.navigateTo(AuthScreen) })
        }
    }

    fun onLogoutClick() {
        executeAction(ShowSignOutDialog)
    }
}