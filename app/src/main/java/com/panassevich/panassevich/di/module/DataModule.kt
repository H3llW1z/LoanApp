package com.panassevich.panassevich.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.panassevich.panassevich.di.annotations.ApplicationScope
import com.panassevich.panassevich.shared.loans.core.data.api.LoanApiFactory
import com.panassevich.panassevich.shared.loans.core.data.api.LoanApiService
import com.panassevich.panassevich.shared.loans.core.data.db.LoansDao
import com.panassevich.panassevich.shared.loans.core.data.db.LoansDatabase
import com.panassevich.panassevich.shared.loans.core.data.implementation.LoanRepositoryImpl
import com.panassevich.panassevich.shared.loans.core.domain.repository.LoanRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindLoanRepository(impl: LoanRepositoryImpl): LoanRepository

    companion object {

        @Provides
        fun providesLoanApiService(): LoanApiService =
            LoanApiFactory.loanApiService

        @Provides
        fun provideLoansDao(application: Application): LoansDao =
            LoansDatabase.getInstance(application).loansDao()

        @Provides
        fun provideSharedPreferences(application: Application): SharedPreferences =
            application.getSharedPreferences("token_prefs", Context.MODE_PRIVATE)

        @Provides
        fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    }
}