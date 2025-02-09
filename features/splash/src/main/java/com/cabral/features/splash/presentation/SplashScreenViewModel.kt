package com.cabral.features.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.core.common.SingletonUser
import com.cabral.core.common.domain.usecase.AutoLoginUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SplashScreenViewModel(
    private val autoLoginUseCase: AutoLoginUseCase,
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    fun getUserLogged(key: String?) {
        if (key != null) {
            autoLoginUseCase(key)
                .catch {
                    _uiEvent.emit(UiEvent.Unlogged)
                }.onEach {
                    SingletonUser.getInstance().setUser(it)
                    _uiEvent.emit(UiEvent.Logged)
                }.launchIn(viewModelScope)
        } else {
            viewModelScope.launch {
                _uiEvent.emit(UiEvent.Unlogged)
            }
        }
    }

}