package com.panassevich.panassevich.navigation.impl

import com.github.terrakok.cicerone.Router
import com.panassevich.panassevich.feature.loans.login.LoginScreenRouter
import com.panassevich.panassevich.feature.loans.registration.getRegistrationScreen
import com.panassevich.panassevich.feature.loans.tutorial.getTutorialScreen
import javax.inject.Inject

class LoginScreenRouterImpl @Inject constructor(
    private val router: Router
) : LoginScreenRouter {
    override fun openTutorialScreen() {
        router.newRootScreen(getTutorialScreen())
    }

    override fun openRegistrationScreen() =
        router.navigateTo(getRegistrationScreen())
}