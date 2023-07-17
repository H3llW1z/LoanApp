package com.panassevich.panassevich.shared.loans.core.data.api

import com.panassevich.panassevich.shared.loans.core.data.api.model.AuthInfoDto
import com.panassevich.panassevich.shared.loans.core.data.api.model.LoanConditionsDto
import com.panassevich.panassevich.shared.loans.core.data.api.model.LoanDto
import com.panassevich.panassevich.shared.loans.core.data.api.model.LoanRequestDto
import com.panassevich.panassevich.shared.loans.core.data.api.model.UserEntityDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface LoanApiService {

    @POST("registration")
    suspend fun register(@Body auth: AuthInfoDto): Response<UserEntityDto>

    @POST("login")
    suspend fun logIn(@Body auth: AuthInfoDto): Response<String>

    @POST("loans")
    suspend fun createNewLoan(
        @Body request: LoanRequestDto,
        @Header("Authorization") token: String
    ): Response<LoanDto>

    @GET("loans/all")
    suspend fun getAllLoans(@Header("Authorization") token: String): Response<List<LoanDto>>

    @GET("loans/conditions")
    suspend fun getLoanConditions(@Header("Authorization") token: String): Response<LoanConditionsDto>

}