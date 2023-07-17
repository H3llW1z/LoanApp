package com.panassevich.panassevich.feature.loans.loancreated

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.panassevich.panassevich.feature.loans.loancreated.ui.LoanCreatedFragment
import com.panassevich.panassevich.shared.loans.core.domain.entity.Loan

fun getLoanCreatedScreen(loan: Loan) =
    FragmentScreen { LoanCreatedFragment.newInstance(loan) }