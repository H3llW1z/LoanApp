package com.panassevich.panassevich.feature.loans.registration.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panassevich.panassevich.component.loans.commonclasses.presentation.state.ErrorEvent
import com.panassevich.panassevich.component.loans.commonclasses.presentation.state.ScreenState
import com.panassevich.panassevich.component.loans.commonclasses.presentation.state.SingleLiveEvent
import com.panassevich.panassevich.feature.loans.registration.RegistrationScreenRouter
import com.panassevich.panassevich.shared.loans.core.domain.entity.AuthInfo
import com.panassevich.panassevich.shared.loans.core.domain.entity.result.RequestError
import com.panassevich.panassevich.shared.loans.core.domain.entity.result.RequestResult
import com.panassevich.panassevich.shared.loans.core.domain.usecase.RegisterUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val router: RegistrationScreenRouter
) : ViewModel() {

    private val _state: MutableLiveData<ScreenState<Unit>> =
        MutableLiveData(ScreenState.Initial)
    val state: LiveData<ScreenState<Unit>> = _state

    private val _errorEvent = SingleLiveEvent<ErrorEvent>()
    val errorEvent: LiveData<ErrorEvent> = _errorEvent

    fun register(name: String, password: String) {
        if (name.isBlank() || password.isBlank()) {
            _state.value = ScreenState.Content(Unit)
            _errorEvent.value = ErrorEvent.EmptyFields
            return
        }

        viewModelScope.launch {
            _state.value = ScreenState.Loading
            val userInfo = AuthInfo(name, password)
            when (val result = registerUseCase(userInfo)) {
                is RequestResult.Success -> _state.value = ScreenState.Success

                is RequestResult.Error -> {
                    _state.value = ScreenState.Error
                    when (val error = result.requestError) {
                        is RequestError.ServerError -> {
                            _errorEvent.value = ErrorEvent.ServerError
                        }

                        is RequestError.NetworkError -> {
                            _errorEvent.value = ErrorEvent.NetworkError
                        }

                        is RequestError.InvalidInput -> {
                            _errorEvent.value = ErrorEvent.EmptyFields
                        }

                        is RequestError.UserAlreadyExists -> {
                            _errorEvent.value = ErrorEvent.UserAlreadyExists
                        }
                        else ->
                            throw RuntimeException("Unexpected error in registration flow: $error")
                    }
                }
            }
        }
    }

    fun openLogInScreen() {
        router.openLogInScreen()
    }
}