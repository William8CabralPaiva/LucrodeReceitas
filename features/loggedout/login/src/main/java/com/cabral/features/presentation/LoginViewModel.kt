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

    private val _uiEvent = Channel<UiState>(Channel.CONFLATED)
    val uiEvent = _uiEvent.receiveAsFlow()

    fun login(email: String?, password: String?) {
        val user = User(email = email, password = password)
        loginUseCase(user).onStart { _uiEvent.send(UiState.StartLoading) }.catch {
            it.message?.let { message ->
                _uiEvent.send(UiState.Error(message))
            }
        }.onEach {
            SingletonUser.getInstance().setUser(it)
            _uiEvent.send(UiState.Success(it))
        }.onCompletion { _uiEvent.send(UiState.StopLoading) }.launchIn(viewModelScope)
    }

    fun googleEmail(email: String?, name: String?) {
        if (email != null && name != null) {
            googleLoginUseCase(email, name).onStart { _uiEvent.send(UiState.GoogleStartLoading) }.catch {
                it.message?.let { message ->
                    _uiEvent.send(UiState.Error(message))
                }
            }.onEach {
                SingletonUser.getInstance().setUser(it)
                _uiEvent.send(UiState.Success(it))
            }.onCompletion { _uiEvent.send(UiState.GoogleStopLoading) }
                .launchIn(viewModelScope)
        }
    }

    fun forgotPassword(email: String?) {
        viewModelScope.launch {
            try {
                if (email != null && EmailUtils.validateEmail(email)) {
                    forgotPasswordUseCase(email)
                    _uiEvent.send(UiState.ForgotPassword)
                }
            } catch (t: UserThrowable) {
                t.message?.let {
                    _uiEvent.send(UiState.ForgotPasswordError(it))
                }
            }
        }
    }
}