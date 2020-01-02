package com.illiarb.tmdbexplorer.coreui.ext

import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun ChipGroup.selectedCount(): Flow<Int> = callbackFlow {
    val checkedListener = ChipGroup.OnCheckedChangeListener { group, _ ->
        offer(group.checkedChipIds.size)
    }

    setOnCheckedChangeListener(checkedListener)

    awaitClose {
        setOnCheckedChangeListener(null)
    }
}