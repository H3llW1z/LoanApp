package com.panassevich.panassevich.shared.loans.core.presentation.transportmodel

import android.os.Parcelable
import com.panassevich.panassevich.shared.loans.core.domain.entity.Loan
import com.panassevich.panassevich.shared.loans.core.domain.entity.LoanStatus
import kotlinx.parcelize.Parcelize

/**
 * Used to send Loan between Fragments
 */
@Parcelize
data class LoanTransportModel(
    val id: Long,
    val amount: Long,
    val date: String,
    val firstName: String,
    val lastName: String,
    val percent: Double,
    val period: Int,
    val phoneNumber: String,
    val status: LoanStatus
) : Parcelable

fun LoanTransportModel.toEntity() = Loan(
    id = id,
    amount = amount,
    date = date,
    firstName = firstName,
    lastName = lastName,
    percent = percent,
    period = period,
    phoneNumber = phoneNumber,
    status = status
)

fun Loan.toTransportModel() = LoanTransportModel(
    id = id,
    amount = amount,
    date = date,
    firstName = firstName,
    lastName = lastName,
    percent = percent,
    period = period,
    phoneNumber = phoneNumber,
    status = status
)
