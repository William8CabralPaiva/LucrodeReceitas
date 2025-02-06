package com.cabral.features.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.core.common.SingletonUser
import com.cabral.core.common.domain.usecase.AutoLoginUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SplashScreenViewModel(
    private val autoLoginUseCase: AutoLoginUseCase,
) : ViewModel() {

    private val _uiEvent = Channel<SplashEvent>(Channel.CONFLATED)
    val uiEvent = _uiEvent.receiveAsFlow()

    fun getUserLogged(key: String?) {
        if (key != null) {
            autoLoginUseCase(key)
                .catch {
                    _uiEvent.send(SplashEvent.Unlogged)
                }.onEach {
                    SingletonUser.getInstance().setUser(it)
                    _uiEvent.send(SplashEvent.Logged)
                }.launchIn(viewModelScope)
        } else {
            viewModelScope.launch {
                _uiEvent.send(SplashEvent.Unlogged)
            }
        }
    }

}