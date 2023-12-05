package com.cabral.features.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.core.common.domain.model.User
import com.cabral.core.common.domain.usecase.AddUserUseCase
import com.cabral.core.common.domain.usecase.LoginUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class LoginViewModel(
    private val addUserUseCase: AddUserUseCase,
    private val loginUseCase: LoginUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    //state flow respostas de back
    //shared flow botao navegar, botao devolve toast, não salva na memoria

    private val _notifySuccess = MutableLiveData<User>()
    val notifySuccess: LiveData<User> = _notifySuccess

    private val _notifyError = MutableLiveData<Unit>()
    val notifyError: LiveData<Unit> = _notifyError

    fun login(){
        val user = User("email aqui", "senha aqui")
        loginUseCase(user).launchIn(viewModelScope)
    }

    fun addUser() {
        val user = User("email aqui", "senha aqui")

//        db.collection("cities").document()
//            .set(user)
//            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
//            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        addUserUseCase(user)
            .catch {
                _notifyError.postValue(Unit)
            }//exception
            .onEach {
                _notifySuccess.postValue(user)
            }//sucesso
            .launchIn(viewModelScope)
    }

    fun addUser2() {
        val user = User("Porchat", "tubuzeira")
        viewModelScope.launch {
            //liga loading
            runCatching { addUserUseCase.invoke2(user) }
                .onFailure {  }
                .onSuccess {  }
                .also {
                    //desliga load
                }
        }

    }

    fun getUser(user: User) {
        addUserUseCase(user)
            .flowOn(dispatcher)
            .onStart {}//start o loader
            .onCompletion { }//desliga laoder
            .catch { }//exception
            .onEach { }//sucesso
            .launchIn(viewModelScope)
//
//        viewModelScope.launch {
//            repository.addUser(user)
//                .flowOn(dispatcher)
//                .onStart {}
//                .onCompletion { }
//                .catch { }
//                .collect {
//
//                }
        //collect
//        }
    }
}