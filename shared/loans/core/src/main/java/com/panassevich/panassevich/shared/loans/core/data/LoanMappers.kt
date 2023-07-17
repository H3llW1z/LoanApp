package com.panassevich.panassevich.shared.loans.core.data

import com.panassevich.panassevich.shared.loans.core.data.api.model.AuthInfoDto
import com.panassevich.panassevich.shared.loans.core.data.api.model.LoanConditionsDto
import com.panassevich.panassevich.shared.loans.core.data.api.model.LoanDto
import com.panassevich.panassevich.shared.loans.core.data.api.model.LoanRequestDto
import com.panassevich.panassevich.shared.loans.core.data.db.model.LoanDbModel
import com.panassevich.panassevich.shared.loans.core.domain.entity.AuthInfo
import com.panassevich.panassevich.shared.loans.core.domain.entity.Loan
import com.panassevich.panassevich.shared.loans.core.domain.entity.LoanConditions
import com.panassevich.panassevich.shared.loans.core.domain.entity.LoanRequest
import com.panassevich.panassevich.shared.loans.core.domain.entity.LoanStatus

fun AuthInfo.toDto() = AuthInfoDto(
    name = name,
    password = password
)

fun LoanRequest.toDto() = LoanRequestDto(
    amount = amount,
    firstName = firstName,
    lastName = lastName,
    percent = percent,
    period = period,
    phoneNumber = phoneNumber
)

fun LoanConditionsDto.toEntity() = LoanConditions(
    maxAmount = maxAmount,
    percent = percent,
    period = period
)

fun LoanDto.toEntity() = Loan(
    id = id,
    amount = amount,
    date = date,
    firstName = firstName,
    lastName = lastName,
    percent = percent,
    period = period,
    phoneNumber = phoneNumber,
    status = convertLoanStatusToEnum(status)
)

fun LoanDto.toDbModel() = LoanDbModel(
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

fun LoanDbModel.toEntity() = Loan(
    id = id,
    amount = amount,
    date = date,
    firstName = firstName,
    lastName = lastName,
    percent = percent,
    period = period,
    phoneNumber = phoneNumber,
    status = convertLoanStatusToEnum(status)
)

fun convertLoanStatusToEnum(status: String) =
    when (status) {
        "APPROVED" -> LoanStatus.APPROVED
        "REGISTERED" -> LoanStatus.REGISTERED
        "REJECTED" -> LoanStatus.REJECTED
        else -> throw RuntimeException("Unknown loan state")
    }