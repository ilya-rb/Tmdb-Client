package com.illiarb.tmdbexplorer.coreui.base.viewmodel

import androidx.lifecycle.ViewModel as AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.illiarb.tmdbexplorer.coreui.common.Message
import com.illiarb.tmdbexplorer.coreui.common.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, Event>(initialState: State) : AndroidViewModel(), ViewModel<State, Event> {

  private val _events = Channel<Event>(Channel.RENDEZVOUS)
  private val _state = ConflatedBroadcastChannel(initialState)
  private val _outEvents = Channel<UiEvent>(Channel.RENDEZVOUS)

  override val events: SendChannel<Event>
    get() = _events

  override val state: Flow<State>
    get() = _state.asFlow()

  override val outEvents: Flow<UiEvent>
    get() = _outEvents.consumeAsFlow()

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

  protected fun showMessage(message: String?) {
    message?.let {
      _outEvents.offer(Message(message))
    }
  }

  protected fun postUiEvent(event: UiEvent) = _outEvents.offer(event)
}