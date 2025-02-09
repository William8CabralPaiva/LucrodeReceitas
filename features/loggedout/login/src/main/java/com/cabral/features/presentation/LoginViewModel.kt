package com.cabral.features.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.arch.EmailUtils
import com.cabral.arch.extensions.UserThrowable
import com.cabral.core.common.SingletonUser
import com.cabral.core.common.domain.model.User
import com.cabral.core.common.domain.usecase.ForgotPasswordUseCase
import com.cabral.core.common.domain.usecase.GoogleLoginUseCase
import com.cabral.core.common.domain.usecase.LoginUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val googleLoginUseCase: GoogleLoginUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiState>()
    val uiEvent: SharedFlow<UiState> = _uiEvent.asSharedFlow()

    fun login(email: String?, password: String?) {
        val user = User(email = email, password = password)
        loginUseCase(user).onStart { _uiEvent.emit(UiState.StartLoading) }.catch {
            it.message?.let { message ->
                _uiEvent.emit(UiState.Error(message))
            }
        }.onEach {
            SingletonUser.getInstance().setUser(it)
            _uiEvent.emit(UiState.Success(it))
        }.onCompletion { _uiEvent.emit(UiState.StopLoading) }.launchIn(viewModelScope)
    }

    fun googleEmail(email: String?, name: String?) {
        if (email != null && name != null) {
            googleLoginUseCase(email, name).onStart { _uiEvent.emit(UiState.GoogleStartLoading) }
                .catch {
                    it.message?.let { message ->
                        _uiEvent.emit(UiState.Error(message))
                    }
                }.onEach {
                SingletonUser.getInstance().setUser(it)
                _uiEvent.emit(UiState.Success(it))
            }.onCompletion { _uiEvent.emit(UiState.GoogleStopLoading) }
                .launchIn(viewModelScope)
        }
    }

    fun forgotPassword(email: String?) {
        viewModelScope.launch {
            try {
                if (email != null && EmailUtils.validateEmail(email)) {
                    forgotPasswordUseCase(email)
                    _uiEvent.emit(UiState.ForgotPassword)
                }
            } catch (t: UserThrowable) {
                t.message?.let {
                    _uiEvent.emit(UiState.ForgotPasswordError(it))
                }
            }
        }
    }
}