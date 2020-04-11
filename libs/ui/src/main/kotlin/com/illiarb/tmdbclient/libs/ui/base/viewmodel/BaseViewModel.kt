package com.illiarb.tmdbclient.libs.ui.base.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel as AndroidViewModel

abstract class BaseViewModel<State, Event>(initialState: State) : AndroidViewModel(), ViewModel<State, Event> {

  private val _events = Channel<Event>(Channel.RENDEZVOUS)
  private val _state = ConflatedBroadcastChannel(initialState)
  private val _errorState = ConflatedBroadcastChannel(ErrorState(message = null))

  override val events: SendChannel<Event>
    get() = _events

  override val state: Flow<State>
    get() = _state.asFlow()

  override val errorState: Flow<ErrorState>
    get() = _errorState.asFlow()

  protected val currentState: State
    get() = _state.value

  protected fun setState(block: State.() -> State) {
    _state.offer(block(_state.value))
  }

  protected fun setErrorState(block: ErrorState.() -> ErrorState) {
    _errorState.offer(block(_errorState.value))
  }

  init {
    viewModelScope.launch {
      _events.consumeEach(::onUiEvent)
    }
  }

  protected open fun onUiEvent(event: Event) = Unit

  protected suspend fun showMessage(message: String?, duration: Long = 2000L) {
    message?.let {
      setErrorState { copy(message = it) }
      delay(duration)
      setErrorState { copy(message = null) }
    }
  }

  data class ErrorState(val message: String?)
}