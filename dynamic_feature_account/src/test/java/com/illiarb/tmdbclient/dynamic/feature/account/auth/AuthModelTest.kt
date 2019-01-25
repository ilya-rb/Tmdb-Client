package com.illiarb.tmdbclient.dynamic.feature.account.auth

import com.illiarb.tmdbclient.dynamic.feature.account.auth.domain.Authenticate
import com.illiarb.tmdbclient.dynamic.feature.account.auth.presentation.AuthModel
import com.illiarb.tmdbclient.dynamic.feature.account.auth.presentation.AuthUiState
import com.illiarb.tmdbcliient.core_test.TestObserver
import com.illiarb.tmdbcliient.core_test.entity.FakeEntityFactory
import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.exception.ValidationException
import com.illiarb.tmdblcient.core.navigation.AccountScreen
import com.illiarb.tmdblcient.core.navigation.Router
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

/**
 * 1. Test on authenticating Loading is showing and auth button is disabled
 * 2. Test on auth failure errors is showing
 * 3. Test on auth success navigated to account
 * 4. Test on inputs changed auth button state changes
 *
 * @author ilya-rb on 24.01.19.
 */
class AuthModelTest {

    private val authenticate = mock<Authenticate>()

    private val router = mock<Router>()

    private val authModel = AuthModel(authenticate, router)

    private val testObserver = TestObserver<AuthUiState>()

    @Before
    fun beforeEachTest() {
        authModel.stateObservable().addObserver(testObserver)
    }

    @After
    fun afterEachTest() {
        testObserver.clearValues()
        authModel.stateObservable().removeObserver(testObserver)
    }

    @Test
    fun `on start authenticate progress is showing and button is disabled`() {
        runBlocking {
            val credentials = FakeEntityFactory.createValidCredentials()

            Mockito
                .`when`(authenticate.executeAsync(credentials))
                .thenReturn(Result.Success(Unit))

            authModel.authenticate(credentials.username, credentials.password)

            testObserver
                .assertValuesCount(3)
                .withPrevious {
                    assertEquals(true, it.isLoading)
                    assertEquals(false, it.authButtonEnabled)
                }
        }
    }

    @Test
    fun `on auth failure errors are showing`() {
        runBlocking {
            val credentials = FakeEntityFactory.createUsernameEmptyCredentials()

            Mockito
                .`when`(authenticate.executeAsync(credentials))
                .thenReturn(Result.Error(ValidationException(mutableListOf())))

            authModel.authenticate(credentials.username, credentials.password)

            testObserver
                // Idle + Loading + Failure
                .assertValuesCount(3)
                .withLatest {
                    assertEquals(true, it.error is ValidationException)
                }
        }
    }

    @Test
    fun `on auth success progress is hidden and user navigates to account`() {
        runBlocking {
            val credentials = FakeEntityFactory.createValidCredentials()

            Mockito
                .`when`(authenticate.executeAsync(credentials))
                .thenReturn(Result.Success(Unit))

            authModel.authenticate(credentials.username, credentials.password)

            testObserver
                // Idle + Loading + Result state
                .assertValuesCount(3)
                .withLatest {
                    assertEquals(false, it.isLoading)
                }

            verify(router).navigateTo(AccountScreen)
        }
    }

    @Test
    fun `on text changes auth button state changes`() {
        authModel.onTextChanged(arrayOf("username", ""))

        testObserver
            // Idle + Result state
            .assertValuesCount(2)
            .withLatest {
                assertEquals(false, it.authButtonEnabled)
            }

        authModel.onTextChanged(arrayOf("username", "password"))

        testObserver
            // + New Result
            .assertValuesCount(3)
            .withLatest {
                assertEquals(true, it.authButtonEnabled)
            }
    }
}