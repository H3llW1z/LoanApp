package com.panassevich.panassevich.navigation.impl

import com.github.terrakok.cicerone.Router
import com.panassevich.panassevich.feature.loans.loanslist.getLoansListScreen
import com.panassevich.panassevich.feature.loans.tutorial.TutorialScreenRouter
import javax.inject.Inject

class TutorialScreenRouterImpl @Inject constructor(
    private val router: Router
) : TutorialScreenRouter {

    override fun openLoansListScreen() {
        router.newRootScreen(getLoansListScreen())
    }
}