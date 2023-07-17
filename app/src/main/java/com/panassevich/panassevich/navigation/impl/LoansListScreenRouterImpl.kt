package com.panassevich.panassevich.navigation.impl

import com.github.terrakok.cicerone.Router
import com.panassevich.panassevich.feature.loans.loandetails.getLoanDetailsScreen
import com.panassevich.panassevich.feature.loans.loanslist.LoansListScreenRouter
import com.panassevich.panassevich.feature.loans.newloan.getNewLoanScreen
import com.panassevich.panassevich.feature.loans.settings.getSettingsScreen
import com.panassevich.panassevich.shared.loans.core.domain.entity.Loan
import javax.inject.Inject

class LoansListScreenRouterImpl @Inject constructor(
    private val router: Router
) : LoansListScreenRouter {
    override fun openLoanDetailsScreen(loan: Loan) {
        router.navigateTo(getLoanDetailsScreen(loan))
    }

    override fun openCreateLoanScreen() {
        router.navigateTo(getNewLoanScreen())
    }

    override fun openSettings() {
        router.navigateTo(getSettingsScreen())
    }
}