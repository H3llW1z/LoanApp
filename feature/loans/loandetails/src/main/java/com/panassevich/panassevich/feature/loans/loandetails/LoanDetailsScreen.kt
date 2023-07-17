package com.panassevich.panassevich.feature.loans.loandetails

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.panassevich.panassevich.feature.loans.loandetails.ui.LoanDetailsFragment
import com.panassevich.panassevich.shared.loans.core.domain.entity.Loan

fun getLoanDetailsScreen(loan: Loan) =
    FragmentScreen { LoanDetailsFragment.newInstance(loan) }