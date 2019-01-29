package com.illiarb.tmdbclient.dynamic.feature.account.profile

import com.illiarb.tmdbclient.dynamic.feature.account.profile.domain.GetProfileUseCase
import com.illiarb.tmdbclient.dynamic.feature.account.profile.domain.SignOutUseCase
import com.illiarb.tmdbclient.dynamic.feature.account.profile.presentation.AccountModel
import com.illiarb.tmdbclient.dynamic.feature.account.profile.presentation.ShowSignOutDialog
import com.illiarb.tmdbcliient.core_test.entity.FakeEntityFactory
import com.illiarb.tmdbcliient.core_test.runWithSubscription
import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.navigation.AuthScreen
import com.illiarb.tmdblcient.core.navigation.MovieDetailsScreen
import com.illiarb.tmdblcient.core.navigation.Router
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito

/**
 * @author ilya-rb on 26.01.19.
 */
@ExperimentalCoroutinesApi
class AccountModelTest {

    private val getProfileUseCase = mock<GetProfileUseCase>()
        .apply {
            runBlocking {
                Mockito
                    .`when`(executeAsync(Unit))
                    .thenReturn(
                        Result.Success(FakeEntityFactory.createFakeAccount())
                    )
            }
        }

    private val signOutUseCase = mock<SignOutUseCase>()

    private val router = mock<Router>()

    private val accountModel = AccountModel(getProfileUseCase, signOutUseCase, router)

    @Test
    fun `on account fetched account is displaying and progress is hidden`() {
        runWithSubscription(accountModel.stateObservable()) { observer ->
            observer.withLatest {
                assertNotNull(it.account)
                assertFalse(it.isLoading)
            }
        }
    }

    @Test
    fun `on sign out confirm auth screen is shown`() {
        runBlocking {
            Mockito
                .`when`(signOutUseCase.executeAsync(Unit))
                .thenReturn(Result.Success(true))

            accountModel.onSignOutConfirm()

            verify(router).navigateTo(AuthScreen)
        }
    }

    @Test
    fun `on sign out confirmation dialog action is executed`() {
        runWithSubscription(accountModel.actionsObservable()) { observer ->
            accountModel.onLogoutClick()

            observer
                .assertValuesCount(1)
                .withLatest {
                    assertTrue(it is ShowSignOutDialog)
                }
        }
    }

    @Test
    fun `on favorites movie click movie screen is opened`() {
        val movie = FakeEntityFactory.createFakeMovie()

        accountModel.onFavoriteMovieClick(movie)

        verify(router).navigateTo(MovieDetailsScreen(movie.id))
    }
}