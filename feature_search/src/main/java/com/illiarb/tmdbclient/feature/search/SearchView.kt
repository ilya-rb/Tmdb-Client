package com.illiarb.tmdbclient.feature.search

import com.illiarb.tmdblcient.core.modules.search.SearchInteractor
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * @author ilya-rb on 27.12.18.
 */
interface SearchView {

    val intents: Observable<SearchIntent>

    val state: Consumer<SearchViewState>

    val sideEffects: Consumer<SearchInteractor.SideEffect>
}