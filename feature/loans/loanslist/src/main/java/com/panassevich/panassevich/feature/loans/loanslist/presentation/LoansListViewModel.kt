package com.panassevich.panassevich.feature.loans.loanslist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panassevich.panassevich.component.loans.commonclasses.presentation.state.ErrorEvent
import com.panassevich.panassevich.component.loans.commonclasses.presentation.state.ScreenState
import com.panassevich.panassevich.component.loans.commonclasses.presentation.state.SingleLiveEvent
import com.panassevich.panassevich.feature.loans.loanslist.LoansListScreenRouter
import com.panassevich.panassevich.shared.loans.core.domain.entity.Loan
import com.panassevich.panassevich.shared.loans.core.domain.entity.result.RequestError
import com.panassevich.panassevich.shared.loans.core.domain.entity.result.RequestResult
import com.panassevich.panassevich.shared.loans.core.domain.usecase.GetAllLoansUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoansListViewModel @Inject constructor(
    private val getAllLoansUseCase: GetAllLoansUseCase,
    private val router: LoansListScreenRouter
) : ViewModel() {

    private val _state: MutableLiveData<ScreenState<List<Loan>>> =
        MutableLiveData(ScreenState.Initial)
    val state: LiveData<ScreenState<List<Loan>>> = _state

    private val _errorEvent = SingleLiveEvent<ErrorEvent>()
    val errorEvent: LiveData<ErrorEvent> = _errorEvent

    fun loadAllLoans() {
        viewModelScope.launch {
            _state.value = ScreenState.Loading
            _state.value = when (val result = getAllLoansUseCase()) {

                is RequestResult.Success -> ScreenState.Content(result.content)

                is RequestResult.Error -> {
                    when (val error = result.requestError) {
                        is RequestError.NetworkError -> {
                            _errorEvent.value = ErrorEvent.NetworkError
                            val loans = error.cachedContent?.sortedByDescending { it.date }
                            if (loans.isNullOrEmpty()) {
                                ScreenState.Error
                            } else ScreenState.Content(loans)
                        }

                        is RequestError.ServerError -> {
                            _errorEvent.value = ErrorEvent.ServerError
                            val loans = error.cachedContent?.sortedByDescending { it.date }
                            if (loans.isNullOrEmpty()) {
                                ScreenState.Error
                            } else ScreenState.Content(loans)
                        }

                        else -> throw RuntimeException("Unexpected error during getting all loans: $error")
                    }
                }
            }
        }
    }

    fun openLoanDetailsScreen(loan: Loan) {
        router.openLoanDetailsScreen(loan)
    }

    fun openCreateLoanScreen() {
        router.openCreateLoanScreen()
    }

    fun openSettings() {
        router.openSettings()
    }
}