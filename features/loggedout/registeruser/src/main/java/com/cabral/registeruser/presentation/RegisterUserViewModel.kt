package com.cabral.registeruser.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.arch.EmailUtils
import com.cabral.arch.PasswordUtils
import com.cabral.arch.extensions.UserThrowable
import com.cabral.core.common.SingletonUser
import com.cabral.core.common.domain.model.User
import com.cabral.core.common.domain.model.UserRegister
import com.cabral.core.common.domain.usecase.AddUserUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class RegisterUserViewModel(
    private val addUserUseCase: AddUserUseCase
) : ViewModel() {

    private val _notifySuccess = MutableLiveData<Unit>()
    val notifySuccess: LiveData<Unit> = _notifySuccess

    private val _notifyError = MutableLiveData<Unit>()
    val notifyError: LiveData<Unit> = _notifyError

    private val _notifyStartLoading = MutableLiveData<Unit>()
    val notifyStartLoading: LiveData<Unit> = _notifyStartLoading

    fun registerUser(
        name: String?,
        email: String?,
        password: String?,
        confirmPassword: String?
    ) {
        if (name.validateName() &&
            EmailUtils.validateEmail(email) &&
            PasswordUtils.validatePassword(password) &&
            equalPassword(password, confirmPassword)
        ) {
            val user = User(email,name,password)
            addUserUseCase(user)
                .onStart {
                    _notifyStartLoading.postValue(Unit)
                }
                .catch {
                    _notifyError.postValue(Unit)
                }.onEach {
                    _notifySuccess.postValue(Unit)
                }.launchIn(viewModelScope)
        }

    }

    private fun String?.validateName(): Boolean {
        if(this!=null) {
            if (trim().length > 1) {
                return true
            }
        }
        throw UserThrowable.UsernameRegisterThrowable()
    }

    private fun equalPassword(password: String?, confirmPassword: String?): Boolean {
        if (password !=null && password == confirmPassword) {
            return true
        }
        throw UserThrowable.NotEqualPasswordThrowable()
    }
}