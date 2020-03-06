package com.illiarb.tmdbexplorer.coreui.common

interface UiEvent

data class Message(val message: String) : UiEvent