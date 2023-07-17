package com.panassevich.panassevich.navigation.impl

import com.github.terrakok.cicerone.Router
import com.panassevich.panassevich.feature.loans.loancreated.getLoanCreatedScreen
import com.panassevich.panassevich.feature.loans.loanslist.getLoansListScreen
import com.panassevich.panassevich.feature.loans.newloan.NewLoanScreenRouter
import com.panassevich.panassevich.shared.loans.core.domain.entity.Loan
import javax.inject.Inject

class NewLoanScreenRouterImpl @Inject constructor(
    private val router: Router
) : NewLoanScreenRouter {
    override fun openLoanCreatedScreen(loan: Loan) {
        router.newRootChain(getLoansListScreen(), getLoanCreatedScreen(loan))
    }
}