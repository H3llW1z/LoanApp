package com.panassevich.panassevich.shared.loans.core.domain.usecase

import com.panassevich.panassevich.shared.loans.core.domain.entity.AuthInfo
import com.panassevich.panassevich.shared.loans.core.domain.repository.LoanRepository
import javax.inject.Inject

class LogInUseCase @Inject constructor(private val repository: LoanRepository) {

    suspend operator fun invoke(authInfo: AuthInfo) =
        repository.logIn(authInfo)
}