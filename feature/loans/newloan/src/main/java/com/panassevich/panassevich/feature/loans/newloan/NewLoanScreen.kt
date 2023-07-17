package com.panassevich.panassevich.feature.loans.newloan

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.panassevich.panassevich.feature.loans.newloan.ui.NewLoanFragment

fun getNewLoanScreen(): FragmentScreen =
    FragmentScreen { NewLoanFragment.newInstance() }