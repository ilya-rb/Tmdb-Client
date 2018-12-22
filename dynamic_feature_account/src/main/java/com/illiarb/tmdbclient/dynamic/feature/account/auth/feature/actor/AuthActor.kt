package com.illiarb.tmdbclient.dynamic.feature.account.auth.feature.actor

import com.badoo.mvicore.element.Actor
import com.illiarb.tmdbclient.dynamic.feature.account.auth.feature.AuthFeature.Action
import com.illiarb.tmdbclient.dynamic.feature.account.auth.feature.AuthFeature.Effect
import com.illiarb.tmdbclient.dynamic.feature.account.auth.feature.AuthViewState
import com.illiarb.tmdblcient.core.exception.ErrorCodes
import com.illiarb.tmdblcient.core.exception.ValidationException
import com.illiarb.tmdblcient.core.modules.auth.Authenticator
import com.illiarb.tmdblcient.core.system.ErrorMessageBag
import com.illiarb.tmdblcient.core.system.SchedulerProvider
import io.reactivex.Observable
import javax.inject.Inject

/**
 * @author ilya-rb on 23.12.18.
 */
class AuthActor @Inject constructor(
    private val authenticator: Authenticator,
    private val validator: Validator,
    private val errorMessageBag: ErrorMessageBag,
    private val schedulerProvider: SchedulerProvider
) : Actor<AuthViewState, Action, Effect> {

    override fun invoke(state: AuthViewState, action: Action): Observable<out Effect> =
        when (action) {
            is Action.ValidateCredentials -> validateCredentials(action.username, action.password)
            is Action.Authenticate ->
                authenticator.authorize(action.username, action.password)
                    .subscribeOn(schedulerProvider.provideIoScheduler())
                    .observeOn(schedulerProvider.provideMainThreadScheduler())
                    .toObservable<Effect>()
                    .map { Effect.AuthenticationResult.Successful }
                    .cast(Effect.AuthenticationResult::class.java)
                    .onErrorReturn(Effect.AuthenticationResult::Failure)
                    .startWith(Effect.AuthenticationResult.InFlight)
        }

    private fun validateCredentials(username: String, password: String): Observable<out Effect> =
        Observable.create { emitter ->
            val errors = mutableListOf<Pair<Int, String>>()

            if (validator.isUsernameEmpty(username)) {
                errors.add(Pair(ErrorCodes.ERROR_USERNAME_EMPTY, errorMessageBag.getUsernameEmptyMessage()))
            }

            if (validator.isPasswordEmpty(password)) {
                errors.add(Pair(ErrorCodes.ERROR_PASSWORD_EMPTY, errorMessageBag.getPasswordEmptyMessage()))
            } else if (!validator.isPasswordLengthCorrect(password)) {
                errors.add(Pair(ErrorCodes.ERROR_PASSWORD_LENGTH, errorMessageBag.getIncorrectPasswordLengthMessage()))
            }

            if (errors.isNotEmpty()) {
                emitter.onNext(Effect.CheckCredentialsResult.Invalid(ValidationException(errors)))
            } else {
                emitter.onNext(Effect.CheckCredentialsResult.Valid)
            }
        }
}