package com.panassevich.panassevich.shared.loans.core.data.api.model

data class LoanRequestDto(
    val amount: Long,
    val firstName: String,
    val lastName: String,
    val percent: Double,
    val period: Int,
    val phoneNumber: String
)


