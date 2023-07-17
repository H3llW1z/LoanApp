package com.panassevich.panassevich.shared.loans.core.domain.entity

data class Loan(
    val id: Long,
    val amount: Long,
    val date: String,
    val firstName: String,
    val lastName: String,
    val percent: Double,
    val period: Int,
    val phoneNumber: String,
    val status: LoanStatus
)
