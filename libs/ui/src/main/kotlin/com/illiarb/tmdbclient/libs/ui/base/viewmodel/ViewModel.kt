package com.illiarb.tmdbclient.libs.ui.base.viewmodel

import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow

interface ViewModel<State, Event> {

  val events: SendChannel<Event>

  val state: Flow<State>
}