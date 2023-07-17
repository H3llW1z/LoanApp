package com.panassevich.panassevich.navigation.impl

import com.github.terrakok.cicerone.Router
import com.panassevich.panassevich.feature.loans.login.getLoginScreen
import com.panassevich.panassevich.feature.loans.registration.RegistrationScreenRouter
import javax.inject.Inject


class RegistrationScreenRouterImpl @Inject constructor(
    private val router: Router
) : RegistrationScreenRouter {
    override fun openLogInScreen() =
        router.backTo(getLoginScreen())
}