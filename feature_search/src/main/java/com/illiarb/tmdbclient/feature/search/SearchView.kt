package com.illiarb.tmdbclient.feature.search

import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * @author ilya-rb on 27.12.18.
 */
interface SearchView {

    val intents: Observable<SearchIntent>

    val state: Consumer<in SearchViewState>
}