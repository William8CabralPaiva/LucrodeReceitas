package com.cabral.features.splash.presentation

sealed interface SplashEvent {
    data object Logged : SplashEvent
    data object Unlogged : SplashEvent
}