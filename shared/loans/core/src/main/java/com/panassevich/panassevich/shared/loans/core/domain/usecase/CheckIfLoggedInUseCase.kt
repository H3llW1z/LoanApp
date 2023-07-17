package com.panassevich.panassevich.shared.loans.core.domain.usecase

import com.panassevich.panassevich.shared.loans.core.domain.repository.LoanRepository
import javax.inject.Inject

class CheckIfLoggedInUseCase @Inject constructor(private val repository: LoanRepository) {

    operator fun invoke() =
        repository.checkIfLoggedIn()
}