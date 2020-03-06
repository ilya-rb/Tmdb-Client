package com.illiarb.tmdbexplorer.coreui.base.viewmodel

import com.illiarb.tmdbexplorer.coreui.common.UiEvent
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow

interface ViewModel<State, Event> {

  val events: SendChannel<Event>

  val outEvents: Flow<UiEvent>

  val state: Flow<State>
}