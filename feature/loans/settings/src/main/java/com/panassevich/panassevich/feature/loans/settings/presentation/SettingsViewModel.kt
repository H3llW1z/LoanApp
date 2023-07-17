package com.panassevich.panassevich.feature.loans.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panassevich.panassevich.feature.loans.settings.SettingsScreenRouter
import com.panassevich.panassevich.shared.loans.core.domain.usecase.LogOutUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase,
    private val router: SettingsScreenRouter
) : ViewModel() {

    fun logOut() {
        viewModelScope.launch {
            val loggedOut = logOutUseCase()
            if (loggedOut) {
                router.openLogInScreen()
            } else throw RuntimeException("Can't log out")
        }
    }

    fun openLoansListScreen() {
        router.openLoansListScreen()
    }
}