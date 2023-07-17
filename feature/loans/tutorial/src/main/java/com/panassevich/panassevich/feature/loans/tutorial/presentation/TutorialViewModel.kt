package com.panassevich.panassevich.feature.loans.tutorial.presentation

import androidx.lifecycle.ViewModel
import com.panassevich.panassevich.feature.loans.tutorial.TutorialScreenRouter
import javax.inject.Inject

class TutorialViewModel @Inject constructor(
    private val router: TutorialScreenRouter
) : ViewModel() {

    fun openLoansList() {
        router.openLoansListScreen()
    }

    fun getInstructions() = Instructions.getInstructions()
}