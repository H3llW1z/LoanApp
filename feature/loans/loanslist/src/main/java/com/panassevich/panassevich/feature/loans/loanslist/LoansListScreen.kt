package com.panassevich.panassevich.feature.loans.loanslist

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.panassevich.panassevich.feature.loans.loanslist.ui.LoansListFragment

fun getLoansListScreen(): FragmentScreen =
    FragmentScreen { LoansListFragment.newInstance() }