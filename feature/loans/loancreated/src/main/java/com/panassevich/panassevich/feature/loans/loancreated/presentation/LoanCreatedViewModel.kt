package com.panassevich.panassevich.feature.loans.loancreated.presentation

import androidx.lifecycle.ViewModel
import com.panassevich.panassevich.feature.loans.loancreated.LoanCreatedScreenRouter
import javax.inject.Inject

class LoanCreatedViewModel @Inject constructor(
    private val router: LoanCreatedScreenRouter
) : ViewModel() {

    fun openLoansListScreen() {
        router.openLoansListScreen()
    }
}