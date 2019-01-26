package com.illiarb.tmdbclient.dynamic.feature.account.auth

import com.illiarb.tmdbclient.dynamic.feature.account.auth.domain.Validator
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author ilya-rb on 25.01.19.
 */
class ValidatorTest {

    private val validator = Validator()

    @Test
    fun `on empty name and password returns false`() {
        assertEquals(true, validator.isUsernameEmpty(" "))
        assertEquals(true, validator.isPasswordEmpty(" "))
    }

    @Test
    fun `on invalid password length returns false`() {
        assertEquals(false, validator.isPasswordLengthCorrect("pas"))
    }

    @Test
    fun `on valid name and password valid result`() {
        assertEquals(false, validator.isUsernameEmpty("user"))
        assertEquals(false, validator.isPasswordEmpty("pass"))
        assertEquals(true, validator.isPasswordLengthCorrect("pass"))
    }
}