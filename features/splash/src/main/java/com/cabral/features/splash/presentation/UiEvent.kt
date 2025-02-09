package com.cabral.features.splash.presentation

sealed interface UiEvent {
    data object Logged : UiEvent
    data object Unlogged : UiEvent
}