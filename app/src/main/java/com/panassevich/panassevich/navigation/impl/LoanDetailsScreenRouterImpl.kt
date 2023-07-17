package com.panassevich.panassevich.navigation.impl

import com.github.terrakok.cicerone.Router
import com.panassevich.panassevich.feature.loans.loandetails.LoanDetailsScreenRouter
import com.panassevich.panassevich.feature.loans.loanslist.getLoansListScreen
import javax.inject.Inject

class LoanDetailsScreenRouterImpl @Inject constructor(
    private val router: Router
) : LoanDetailsScreenRouter {
    override fun openLoansListScreen() {
        router.backTo(getLoansListScreen())
    }
}