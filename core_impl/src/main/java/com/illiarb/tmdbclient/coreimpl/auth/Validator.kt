package com.illiarb.tmdbclient.coreimpl.auth

import javax.inject.Inject

/**
 * @author ilya-rb on 27.11.18.
 */
class Validator @Inject constructor() {

    fun isUsernameEmpty(username: String): Boolean = username.trim().isEmpty()

    fun isPasswordEmpty(password: String): Boolean = password.trim().isEmpty()

    fun isPasswordLengthCorrect(password: String): Boolean = password.trim().length >= MIN_PASSWORD_LENGTH

    companion object {
        const val MIN_PASSWORD_LENGTH = 4
    }
}