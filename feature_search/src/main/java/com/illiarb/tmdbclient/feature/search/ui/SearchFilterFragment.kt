package com.illiarb.tmdbclient.feature.search.ui

import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * @author ilya-rb on 20.01.19.
 */
class SearchFilterFragment : BottomSheetDialogFragment() {

    companion object {

        fun newInstance(): SearchFilterFragment = SearchFilterFragment()
    }
}