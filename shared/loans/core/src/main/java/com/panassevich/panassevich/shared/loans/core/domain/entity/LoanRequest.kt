package com.panassevich.panassevich.shared.loans.core.domain.entity

data class LoanRequest(
    val amount: Long,
    val firstName: String,
    val lastName: String,
    val percent: Double,
    val period: Int,
    val phoneNumber: String
)


