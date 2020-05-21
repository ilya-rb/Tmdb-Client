package com.illiarb.tmdbclient.libs.ui.base.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel as AndroidViewModel

abstract class BaseViewModel<State, Event>(initialState: State) : AndroidViewModel(),
  ViewModel<State, Event> {

  private val _state = MutableStateFlow(initialState)
  private val _events = Channel<Event>(Channel.RENDEZVOUS)

  override val events: SendChannel<Event>
    get() = _events

  override val state: StateFlow<State>
    get() = _state

  protected val currentState: State
    get() = _state.value

  protected fun setState(block: State.() -> State) {
    _state.value = block(_state.value)
  }

  init {
    viewModelScope.launch {
      _events.consumeEach(::onUiEvent)
    }
  }

  protected open fun onUiEvent(event: Event) = Unit
}