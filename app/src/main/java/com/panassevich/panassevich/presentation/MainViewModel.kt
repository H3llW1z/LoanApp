package com.panassevich.panassevich.presentation

import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.panassevich.panassevich.feature.loans.loanslist.getLoansListScreen
import com.panassevich.panassevich.feature.loans.login.getLoginScreen
import com.panassevich.panassevich.shared.loans.core.domain.usecase.CheckIfLoggedInUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val router: Router,
    private val navigatorHolder: NavigatorHolder,
    private val checkIfLoggedInUseCase: CheckIfLoggedInUseCase
) : ViewModel() {

    init {
        openStartScreen()
    }

    private fun openStartScreen() {
        if (checkIfLoggedInUseCase()) {
            router.newRootScreen(getLoansListScreen())
        } else {
            router.newRootScreen(getLoginScreen())
        }
    }

    fun setNavigator(navigator: Navigator) {
        navigatorHolder.setNavigator(navigator)
    }

    fun removeNavigator() {
        navigatorHolder.removeNavigator()
    }
}