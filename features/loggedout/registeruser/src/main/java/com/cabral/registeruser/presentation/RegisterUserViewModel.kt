package com.cabral.registeruser.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.arch.EmailUtils
import com.cabral.arch.PasswordUtils
import com.cabral.arch.extensions.UserThrowable
import com.cabral.core.common.domain.model.User
import com.cabral.core.common.domain.usecase.AddUserUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _uiState = MutableStateFlow<UiState>(UiState.DefaultFieldsState)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun registerUser(
        name: String?, email: String?, password: String?, confirmPassword: String?
    ) {
        try {
            if (validateFields(name, email, password, confirmPassword)) {
                val user = User(email, name, password)
                addUserUseCase(user).onStart {
                    _uiEvent.emit(UiEvent.StartLoading)
                }.catch {
                    _uiEvent.emit(UiEvent.Error)
                }.onEach {
                    _uiEvent.emit(UiEvent.Success)
                }.launchIn(viewModelScope)
            }
        } catch (error: UserThrowable) {
            handleUserThrowable(error)
        }

    }

    private fun handleUserThrowable(error: UserThrowable) {
        viewModelScope.launch {
            when (error) {
                is UserThrowable.AuthenticatePasswordThrowable -> {
                    _uiState.emit(UiState.ErrorPassword(error.message))
                }

                is UserThrowable.AuthenticateEmailThrowable -> {
                    _uiState.emit(UiState.ErrorEmail(error.message))
                }

                is UserThrowable.UsernameRegisterThrowable -> {
                    _uiState.emit(UiState.ErrorUsername(error.message))
                }

                else -> {
                    error.message?.let { UiState.ErrorConfirmPassword(it) }
                        ?.let { _uiState.emit(it) }
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

    private fun validateFields(
        name: String?,
        email: String?,
        password: String?,
        confirmPassword: String?
    ): Boolean {
        return (name.validateName() && EmailUtils.validateEmail(email) && PasswordUtils.validatePassword(
            password
        ) && equalPassword(password, confirmPassword)
                )
    }

    private fun equalPassword(password: String?, confirmPassword: String?): Boolean {
        if (password != null && password == confirmPassword) {
            return true
        }
        throw UserThrowable.NotEqualPasswordThrowable()
    }

    fun setDefaultFieldsState() {
        viewModelScope.launch {
            _uiState.emit(UiState.DefaultFieldsState)
        }
    }
}