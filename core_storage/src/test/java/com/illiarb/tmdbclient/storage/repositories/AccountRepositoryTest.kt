package com.illiarb.tmdbclient.storage.repositories

import com.illiarb.tmdbclient.storage.local.PersistableStorage
import com.illiarb.tmdbclient.storage.model.AccountModel
import com.illiarb.tmdbclient.storage.network.api.service.AccountService
import com.illiarb.tmdbcliient.core_test.TestDispatcherProvider
import com.illiarb.tmdbcliient.core_test.entity.FakeEntityFactory
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito

/**
 * @author ilya-rb on 21.02.19.
 */
@ExperimentalCoroutinesApi
class AccountRepositoryTest {

    private val accountService = mock<AccountService>()
    private val storage = mock<PersistableStorage>()

    private val accountRepository = AccountRepositoryImpl(
        accountService,
        storage,
        com.tmdbclient.service_tmdb.mappers.AccountMapper(),
        TestDispatcherProvider(),
        com.tmdbclient.service_tmdb.mappers.MovieMapper(
            com.tmdbclient.service_tmdb.mappers.GenreMapper(),
            com.tmdbclient.service_tmdb.mappers.PersonMapper(),
            com.tmdbclient.service_tmdb.mappers.ReviewMapper()
        )
    )

    @Test
    fun `if user logged in cached account is returned`() {
        // Create Fake account to emulate that user is authorized
        val account = createAccountModel()
        Mockito.`when`(storage.getCurrentAccount()).thenReturn(account)

        runBlocking {
            accountRepository.getCurrentAccount()
        }

        // Verify that account gets only from cache
        verify(storage).getCurrentAccount()
        // And no network calls
        verifyZeroInteractions(accountService)
    }

    @Test
    fun `on account fetched from api it caches in storage`() {
        val session = "session_id"
        val account = createAccountModel()

        Mockito.`when`(storage.getCurrentAccount()).thenReturn(AccountModel.createNonExistent())
        Mockito.`when`(storage.getSessionId()).thenReturn(session)
        Mockito
            .`when`(accountService.getAccountDetailsAsync(session))
            .thenReturn(CompletableDeferred(account))

        runBlocking {
            accountRepository.getCurrentAccount()
        }

        // Check that we fetched account from api
        @Suppress("DeferredResultUnused")
        verify(accountService).getAccountDetailsAsync(session)
        // And then store it in cache
        verify(storage).storeAccount(account)
    }

    @Test
    fun `on sign out account data is cleared`() {
        runBlocking {
            accountRepository.clearAccountData()
        }
        verify(storage).clearAccountData()
    }

    private fun createAccountModel(): AccountModel =
        with(FakeEntityFactory.createFakeAccount()) {
            AccountModel(id, name, username)
        }
}