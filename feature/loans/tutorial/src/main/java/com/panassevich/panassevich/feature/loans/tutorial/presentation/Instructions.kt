package com.panassevich.panassevich.feature.loans.tutorial.presentation

import com.panassevich.panassevich.component.loans.resources.R
import com.panassevich.panassevich.feature.loans.tutorial.R as LocalR

object Instructions {

    fun getInstructions(): List<InstructionItem> {
        val greeting = InstructionItem(
            LocalR.drawable.ic_loan,
            R.string.tutorial_1_title,
            R.string.tutorial_1_description
        )
        val loansListScreenInstruction = InstructionItem(
            LocalR.drawable.sample_main_screen,
            R.string.tutorial_2_title,
            R.string.tutorial_2_description
        )
        val newLoanScreenInstruction = InstructionItem(
            LocalR.drawable.sample_new_loan_screen,
            R.string.tutorial_3_title,
            R.string.tutorial_3_description
        )
        val loanDetailsScreenInstruction = InstructionItem(
            LocalR.drawable.sample_loan_details_screen,
            R.string.tutorial_4_title,
            R.string.tutorial_4_description
        )
        return listOf(
            greeting,
            loansListScreenInstruction,
            newLoanScreenInstruction,
            loanDetailsScreenInstruction
        )
    }
}