package com.cabral.features.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.arch.EmailUtils
import com.cabral.arch.extensions.UserThrowable
import com.cabral.core.common.SingletonUser
import com.cabral.core.common.domain.model.User
import com.cabral.core.common.domain.usecase.AddUserUseCase
import com.cabral.core.common.domain.usecase.ForgotPasswordUseCase
import com.cabral.core.common.domain.usecase.GoogleLoginUseCase
import com.cabral.core.common.domain.usecase.LoginUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class LoginViewModel(
    private val addUserUseCase: AddUserUseCase,
    private val loginUseCase: LoginUseCase,
    private val googleLoginUseCase: GoogleLoginUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _notifySuccess = MutableLiveData<User>()
    val notifySuccess: LiveData<User> = _notifySuccess

    private val _notifyStartLoading = MutableLiveData<Unit>()
    val notifyStartLoading: LiveData<Unit> = _notifyStartLoading

    private val _notifyError = MutableLiveData<String>()
    val notifyError: LiveData<String> = _notifyError

    private val _notifyForgotPassword = MutableLiveData<Unit>()
    val notifyForgotPassword: LiveData<Unit> = _notifyForgotPassword

    private val _notifyErrorForgotPassword = MutableLiveData<String>()
    val notifyErrorForgotPassword: LiveData<String> = _notifyErrorForgotPassword

    fun login(email: String?, password: String?) {
        val user = User(email = email, password = password)

        loginUseCase(user)
            //.onStart { _notifyStartLoading.postValue(Unit) }
            .catch {
                _notifyError.postValue(it.message)
            }.onEach {
                SingletonUser.getInstance().setUser(it)
                _notifySuccess.postValue(user)
            }
            .launchIn(viewModelScope)
    }

    fun googleEmail(email: String?, name: String?) {
        if (email != null && name != null) {
            googleLoginUseCase(email, name)
                .catch {
                    _notifyError.postValue(it.message)
                }.onEach {
                    SingletonUser.getInstance().setUser(it)
                    _notifySuccess.postValue(it)
                }.launchIn(viewModelScope)
        }
    }

    fun forgotPassword(email: String?) {
        viewModelScope.launch {
            try {
                if (email != null && EmailUtils.validateEmail(email)) {
                    forgotPasswordUseCase(email)
                    _notifyForgotPassword.postValue(Unit)
                }
            } catch (t: UserThrowable) {
                _notifyErrorForgotPassword.postValue(t.message)
            }
        }
    }
}