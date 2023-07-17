package com.panassevich.panassevich.feature.loans.login.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panassevich.panassevich.component.loans.commonclasses.presentation.state.ErrorEvent
import com.panassevich.panassevich.component.loans.commonclasses.presentation.state.ScreenState
import com.panassevich.panassevich.component.loans.commonclasses.presentation.state.SingleLiveEvent
import com.panassevich.panassevich.feature.loans.login.LoginScreenRouter
import com.panassevich.panassevich.shared.loans.core.domain.entity.AuthInfo
import com.panassevich.panassevich.shared.loans.core.domain.entity.result.RequestError
import com.panassevich.panassevich.shared.loans.core.domain.entity.result.RequestResult
import com.panassevich.panassevich.shared.loans.core.domain.usecase.LogInUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val logInUseCase: LogInUseCase,
    private val router: LoginScreenRouter
) : ViewModel() {

    private val _state: MutableLiveData<ScreenState<Unit>> =
        MutableLiveData(ScreenState.Initial)
    val state: LiveData<ScreenState<Unit>> = _state

    private val _errorEvent = SingleLiveEvent<ErrorEvent>()
    val errorEvent: LiveData<ErrorEvent> = _errorEvent

    fun logIn(name: String, password: String) {
        if (name.isBlank() || password.isBlank()) {
            _state.value = ScreenState.Content(Unit)
            _errorEvent.value = ErrorEvent.EmptyFields
            return
        }

        viewModelScope.launch {
            _state.value = ScreenState.Loading
            val userInfo = AuthInfo(name, password)
            when (val result = logInUseCase(userInfo)) {
                is RequestResult.Success -> _state.value = ScreenState.Success

                is RequestResult.Error -> {
                    _state.value = ScreenState.Error
                    when (val error = result.requestError) {
                        is RequestError.NetworkError -> {
                            _errorEvent.value = ErrorEvent.NetworkError
                        }

                        is RequestError.ServerError -> {
                            _errorEvent.value = ErrorEvent.ServerError
                        }

                        is RequestError.UserNotFound -> {
                            _errorEvent.value = ErrorEvent.UserNotFound
                        }

                        is RequestError.InvalidInput -> {
                            _errorEvent.value = ErrorEvent.EmptyFields
                        }

                        else ->
                            throw RuntimeException("Unexpected error in login flow: $error")
                    }
                }
            }
        }
    }

    fun openRegistrationScreen() {
        router.openRegistrationScreen()
    }

    fun openTutorialScreen() {
        router.openTutorialScreen()
    }
}