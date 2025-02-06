package com.cabral.registeruser.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.arch.EmailUtils
import com.cabral.arch.PasswordUtils
import com.cabral.arch.extensions.UserThrowable
import com.cabral.core.common.domain.model.User
import com.cabral.core.common.domain.usecase.AddUserUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class RegisterUserViewModel(
    private val addUserUseCase: AddUserUseCase
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    fun registerUser(
        name: String?, email: String?, password: String?, confirmPassword: String?
    ) {
        try {
            if (name.validateName() && EmailUtils.validateEmail(email) && PasswordUtils.validatePassword(
                    password
                ) && equalPassword(password, confirmPassword)
            ) {
                val user = User(email, name, password)
                addUserUseCase(user).onStart {
                    _uiEvent.emit(UiEvent.StartLoading)
                }.catch {
                    _uiEvent.emit(UiEvent.Error)
                }.onEach {
                    _uiEvent.emit(UiEvent.Success)
                }.launchIn(viewModelScope)
            }
        } catch (error: Throwable) {
            viewModelScope.launch {
                when (error) {
                    is UserThrowable.AuthenticatePasswordThrowable -> {
                        _uiEvent.emit(UiEvent.ErrorPassword(error.message))
                    }

                    is UserThrowable.AuthenticateEmailThrowable -> {
                        _uiEvent.emit(UiEvent.ErrorEmail(error.message))
                    }

                    is UserThrowable.UsernameRegisterThrowable -> {
                        _uiEvent.emit(UiEvent.ErrorUsername(error.message))
                    }

                    else -> {
                        error.message?.let {
                            _uiEvent.emit(UiEvent.ErrorConfirmPassword(it))
                        }
                    }
                }
            }
        }

    }

    private fun String?.validateName(): Boolean {
        if (this != null) {
            if (trim().length > 1) {
                return true
            }
        }
        throw UserThrowable.UsernameRegisterThrowable()
    }

    private fun equalPassword(password: String?, confirmPassword: String?): Boolean {
        if (password != null && password == confirmPassword) {
            return true
        }
        throw UserThrowable.NotEqualPasswordThrowable()
    }
}