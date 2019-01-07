package com.illiarb.tmdblcient.core.domain.auth

import javax.inject.Inject

/**
 * @author ilya-rb on 23.12.18.
 */
class Validator @Inject constructor() {

    fun isUsernameEmpty(username: String): Boolean = username.trim().isEmpty()

    fun isPasswordEmpty(password: String): Boolean = password.trim().isEmpty()

    fun isPasswordLengthCorrect(password: String): Boolean = password.trim().length >= MIN_PASSWORD_LENGTH

    companion object {
        const val MIN_PASSWORD_LENGTH = 4
    }

//    private fun validateCredentials(username: String, password: String): Observable<out Effect> =
//        Observable.create { emitter ->
//            val errors = mutableListOf<Pair<Int, String>>()
//
//            if (validator.isUsernameEmpty(username)) {
//                errors.add(Pair(ErrorCodes.ERROR_USERNAME_EMPTY, errorMessageBag.getUsernameEmptyMessage()))
//            }
//
//            if (validator.isPasswordEmpty(password)) {
//                errors.add(Pair(ErrorCodes.ERROR_PASSWORD_EMPTY, errorMessageBag.getPasswordEmptyMessage()))
//            } else if (!validator.isPasswordLengthCorrect(password)) {
//                errors.add(Pair(ErrorCodes.ERROR_PASSWORD_LENGTH, errorMessageBag.getIncorrectPasswordLengthMessage()))
//            }
//
//            if (errors.isNotEmpty()) {
//                emitter.onNext(Effect.CheckCredentialsResult.Invalid(ValidationException(errors)))
//            } else {
//                emitter.onNext(Effect.CheckCredentialsResult.Valid)
//            }
//        }
}