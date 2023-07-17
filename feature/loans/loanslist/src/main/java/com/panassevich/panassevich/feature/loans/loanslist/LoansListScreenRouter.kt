package com.panassevich.panassevich.feature.loans.loanslist

import com.panassevich.panassevich.shared.loans.core.domain.entity.Loan

interface LoansListScreenRouter {

    fun openLoanDetailsScreen(loan: Loan)

    fun openCreateLoanScreen()

    fun openSettings()
}