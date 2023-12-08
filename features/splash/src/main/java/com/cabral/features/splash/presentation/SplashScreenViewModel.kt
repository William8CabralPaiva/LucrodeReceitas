package com.cabral.features.splash.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.core.common.SingletonUser
import com.cabral.core.common.domain.usecase.AutoLoginUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SplashScreenViewModel(
    private val autoLoginUseCase: AutoLoginUseCase,
) : ViewModel() {

    private val _notifyLogged = MutableLiveData<Boolean>()
    val notifyLogged: LiveData<Boolean> = _notifyLogged

    fun getUserLogged(key: String?) {
        if (key != null) {
            autoLoginUseCase(key)
                .catch {
                    _notifyLogged.postValue(false)
                }.onEach {
                    SingletonUser.getInstance().setUser(it)
                    _notifyLogged.postValue(true)
                }
                .launchIn(viewModelScope)
        } else {
            _notifyLogged.postValue(false)
        }
    }
}