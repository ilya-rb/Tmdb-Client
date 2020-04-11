package com.illiarb.tmdbclient.functional.recyclerview

import android.view.View
import com.agoda.kakao.recycler.KRecyclerItem
import org.hamcrest.Matcher

class MovieItem(parent: Matcher<View>) : KRecyclerItem<MovieItem>(parent)