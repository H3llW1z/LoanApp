package com.panassevich.panassevich.shared.loans.core.domain.usecase

import com.panassevich.panassevich.shared.loans.core.domain.repository.LoanRepository
import javax.inject.Inject

class GetLoanConditionsUseCase @Inject constructor(private val repository: LoanRepository) {

    suspend operator fun invoke() =
        repository.getLoanConditions()
}