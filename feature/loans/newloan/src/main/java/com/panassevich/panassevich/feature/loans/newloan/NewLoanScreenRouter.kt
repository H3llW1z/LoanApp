package com.panassevich.panassevich.feature.loans.newloan

import com.panassevich.panassevich.shared.loans.core.domain.entity.Loan

interface NewLoanScreenRouter {

    fun openLoanCreatedScreen(loan: Loan)
}