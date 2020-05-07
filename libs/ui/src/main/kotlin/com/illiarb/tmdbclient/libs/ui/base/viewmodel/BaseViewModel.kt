package com.illiarb.tmdbclient.libs.ui.base.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel as AndroidViewModel

abstract class BaseViewModel<State, Event>(initialState: State) : AndroidViewModel(),
  ViewModel<State, Event> {

  private val _events = Channel<Event>(Channel.RENDEZVOUS)
  private val _state = ConflatedBroadcastChannel(initialState)

  override val events: SendChannel<Event>
    get() = _events

  override val state: Flow<State>
    get() = _state.asFlow()

  protected val currentState: State
    get() = _state.value

  protected fun setState(block: State.() -> State) {
    _state.offer(block(_state.value))
  }

  init {
    viewModelScope.launch {
      _events.consumeEach(::onUiEvent)
    }
  }

  protected open fun onUiEvent(event: Event) = Unit
}