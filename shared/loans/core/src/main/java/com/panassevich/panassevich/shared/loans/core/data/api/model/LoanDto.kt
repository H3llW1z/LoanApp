package com.panassevich.panassevich.shared.loans.core.data.api.model

import com.google.gson.annotations.SerializedName

data class LoanDto(
    val id: Long,
    val amount: Long,
    val date: String,
    val firstName: String,
    val lastName: String,
    val percent: Double,
    val period: Int,
    val phoneNumber: String,
    @SerializedName("state")
    val status: String
)