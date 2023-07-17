package com.panassevich.panassevich.navigation.impl

import com.github.terrakok.cicerone.Router
import com.panassevich.panassevich.feature.loans.loanslist.getLoansListScreen
import com.panassevich.panassevich.feature.loans.login.getLoginScreen
import com.panassevich.panassevich.feature.loans.settings.SettingsScreenRouter
import javax.inject.Inject

class SettingsScreenRouterImpl @Inject constructor(
    private val router: Router
) : SettingsScreenRouter {

    override fun openLogInScreen() {
        router.newRootScreen(getLoginScreen())
    }

    override fun openLoansListScreen() {
        router.backTo(getLoansListScreen())
    }
}