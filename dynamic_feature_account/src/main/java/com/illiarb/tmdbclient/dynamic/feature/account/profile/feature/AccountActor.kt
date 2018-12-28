package com.illiarb.tmdbclient.dynamic.feature.account.profile.feature

import com.badoo.mvicore.element.Actor
import com.illiarb.tmdbclient.dynamic.feature.account.auth.feature.AuthFeature
import com.illiarb.tmdbclient.dynamic.feature.account.profile.feature.AccountFeature.Action
import com.illiarb.tmdbclient.dynamic.feature.account.profile.feature.AccountFeature.Effect
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.modules.account.AccountRepository
import com.illiarb.tmdblcient.core.system.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.Collections
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * @author ilya-rb on 24.12.18.
 */
class AccountActor @Inject constructor(
    private val schedulerProvider: SchedulerProvider,
    private val accountRepository: AccountRepository
) : Actor<AccountState, Action, Effect> {

    override fun invoke(state: AccountState, action: Action): Observable<out Effect> =
        when (action) {
            is Action.SignOut -> signOut()
            is Action.ShowAccount -> showAccount()
            is Action.ShowMovieDetails -> showMovieDetails(action.id)
        }

    private fun signOut(): Observable<out Effect> =
        accountRepository.clearAccountData()
            .subscribeOn(schedulerProvider.provideIoScheduler())
            .observeOn(schedulerProvider.provideMainThreadScheduler())
            .andThen(ObservableSource<AuthFeature.Effect> { Effect.SignOut.Success })
            .cast(Effect.SignOut::class.java)
            .startWith(Effect.SignOut.InFlight)
            .onErrorReturn(Effect.SignOut::Failure)

    private fun showAccount(): Observable<out Effect> =
        accountRepository.getCurrentAccount()
            .flatMap { account ->
                Single
                    .zip(
                        accountRepository.getFavoriteMovies(account.id)
                            .onErrorResumeNext(Single.just(Collections.emptyList())),

                        accountRepository.getRatedMovies(account.id)
                            .onErrorResumeNext(Single.just(Collections.emptyList())),

                        BiFunction { favoriteMovies: List<Movie>, ratedMovies: List<Movie> ->
                            account.copy(
                                averageRating = createAverageRating(ratedMovies),
                                favoriteMovies = favoriteMovies
                            )
                        }
                    )
            }
            .subscribeOn(schedulerProvider.provideIoScheduler())
            .observeOn(schedulerProvider.provideMainThreadScheduler())
            .flatMapObservable { Observable.just(Effect.Account.Success(it)) }
            .cast(Effect.Account::class.java)
            .onErrorReturn(Effect.Account::Failure)
            .startWith(Effect.Account.InFlight)

    private fun showMovieDetails(id: Int): Observable<out Effect> = Observable.just(Effect.ShowMovieDetails(id))

    private fun createAverageRating(ratedMovies: List<Movie>): Int =
        ratedMovies
            .map { it.rating }
            .average()
            .roundToInt() * 10
}