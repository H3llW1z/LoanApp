package com.panassevich.panassevich.feature.loans.newloan.presentation

data class RequestInputData(
    val amount: String,
    val name: String,
    val lastName: String,
    val phone: String
)