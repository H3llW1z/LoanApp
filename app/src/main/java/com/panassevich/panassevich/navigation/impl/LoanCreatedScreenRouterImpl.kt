package com.panassevich.panassevich.navigation.impl

import com.github.terrakok.cicerone.Router
import com.panassevich.panassevich.feature.loans.loancreated.LoanCreatedScreenRouter
import javax.inject.Inject

class LoanCreatedScreenRouterImpl @Inject constructor(
    private val router: Router
) : LoanCreatedScreenRouter {
    override fun openLoansListScreen() {
        router.exit()
    }
}