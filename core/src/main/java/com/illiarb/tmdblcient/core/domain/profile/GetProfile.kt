package com.illiarb.tmdblcient.core.domain.profile

import com.illiarb.tmdblcient.core.entity.Account
import com.illiarb.tmdblcient.core.system.NonBlocking

/**
 * @author ilya-rb on 07.01.19.
 */
interface GetProfile {

    @NonBlocking
    suspend operator fun invoke(): Account

//    private fun signOut(): Observable<out Effect> =
//        accountRepository.clearAccountData()
//            .subscribeOn(schedulerProvider.provideIoScheduler())
//            .observeOn(schedulerProvider.provideMainThreadScheduler())
//            .andThen(ObservableSource<AuthFeature.Effect> { Effect.SignOut.Success })
//            .cast(Effect.SignOut::class.java)
//            .startWith(Effect.SignOut.InFlight)
//            .onErrorReturn(Effect.SignOut::Failure)
//
//    private fun showAccount(): Observable<out Effect> =
//        accountRepository.getCurrentAccount()
//            .flatMap { account ->
//                Single
//                    .zip(
//                        accountRepository.getFavoriteMovies(account.id)
//                            .onErrorResumeNext(Single.just(Collections.emptyList())),
//
//                        accountRepository.getRatedMovies(account.id)
//                            .onErrorResumeNext(Single.just(Collections.emptyList())),
//
//                        BiFunction { favoriteMovies: List<Movie>, ratedMovies: List<Movie> ->
//                            account.copy(
//                                averageRating = createAverageRating(ratedMovies),
//                                favoriteMovies = favoriteMovies
//                            )
//                        }
//                    )
//            }
//            .subscribeOn(schedulerProvider.provideIoScheduler())
//            .observeOn(schedulerProvider.provideMainThreadScheduler())
//            .flatMapObservable { Observable.just(Effect.Account.Success(it)) }
//            .cast(Effect.Account::class.java)
//            .onErrorReturn(Effect.Account::Failure)
//            .startWith(Effect.Account.InFlight)
//
//    private fun showMovieDetails(id: Int): Observable<out Effect> = Observable.just(Effect.ShowMovieDetails(id))
//
//    private fun createAverageRating(ratedMovies: List<Movie>): Int =
//        ratedMovies
//            .map { it.rating }
//            .average()
//            .roundToInt() * 10
}