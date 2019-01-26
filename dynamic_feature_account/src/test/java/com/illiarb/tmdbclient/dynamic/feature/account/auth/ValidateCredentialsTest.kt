package com.illiarb.tmdbclient.dynamic.feature.account.auth

import com.illiarb.tmdbclient.dynamic.feature.account.auth.domain.ValidateCredentials
import com.illiarb.tmdbclient.dynamic.feature.account.auth.domain.Validator
import com.illiarb.tmdbcliient.core_test.entity.FakeEntityFactory
import com.illiarb.tmdblcient.core.entity.UserCredentials
import com.illiarb.tmdblcient.core.exception.ErrorCodes
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * @author ilya-rb on 25.01.19.
 */
class ValidateCredentialsTest {

    private val validateCredentials = ValidateCredentials(Validator(), mock())

    @Test
    fun `on invalid credentials error codes is returned`() {

        // Validate empty credentials
        val emptyCredentials = UserCredentials("", "")
        val result = validateCredentials.executeBlocking(emptyCredentials)

        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)

        assertTrue(result.find { it.first == ErrorCodes.ERROR_USERNAME_EMPTY } != null)
        assertTrue(result.find { it.first == ErrorCodes.ERROR_PASSWORD_EMPTY } != null)

        // Valid for password length
        val passwordCredentials = FakeEntityFactory.createPasswordInvalidCredentials()
        val passwordResult = validateCredentials.executeBlocking(passwordCredentials)

        assertTrue(passwordResult.find { it.first == ErrorCodes.ERROR_PASSWORD_LENGTH } != null)
    }

    @Test
    fun `on valid credentials success is returned`() {
        val validCredentials = FakeEntityFactory.createValidCredentials()
        val result = validateCredentials.executeBlocking(validCredentials)
        assertTrue(result.isEmpty())
    }
}