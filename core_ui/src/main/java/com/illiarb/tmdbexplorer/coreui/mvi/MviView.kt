package com.illiarb.tmdbexplorer.coreui.mvi

import io.reactivex.Observable

/**
 * Object representing a UI that will
 * a) emit its intents to a view model,
 * b) subscribes to a view model for rendering its UI.
 *
 * @param I Top class of the [MviIntent] that the [MviView] will be emitting.
 * @param S Top class of the [MviState] the [MviView] will be subscribing to.
 */
interface MviView<I : MviIntent, in S : MviState, N : MviNews> {
    /**
     * Unique [Observable] used by the [MviState]
     * to listen to the [MviView].
     * All the [MviView]'s [MviIntent]s must go through this [Observable].
     */
    fun intents(): Observable<I>

    /**
     * Entry point for the [MviView] to render itself based on a [MviState].
     */
    fun render(state: S)

    /**
     * Entry point to process single-live events
     */
    fun processNews(news: N)
}
