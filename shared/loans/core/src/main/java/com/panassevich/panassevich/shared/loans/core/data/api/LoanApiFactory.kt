package com.panassevich.panassevich.shared.loans.core.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LoanApiFactory {

    private const val BASE_URL = "https://shiftlab.cft.ru:7777/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(ToStringConverterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    val loanApiService: LoanApiService = retrofit.create(LoanApiService::class.java)
}