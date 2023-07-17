package com.panassevich.panassevich.di.module

import com.panassevich.panassevich.feature.loans.loancreated.LoanCreatedScreenRouter
import com.panassevich.panassevich.feature.loans.loandetails.LoanDetailsScreenRouter
import com.panassevich.panassevich.feature.loans.loanslist.LoansListScreenRouter
import com.panassevich.panassevich.feature.loans.login.LoginScreenRouter
import com.panassevich.panassevich.feature.loans.newloan.NewLoanScreenRouter
import com.panassevich.panassevich.feature.loans.registration.RegistrationScreenRouter
import com.panassevich.panassevich.feature.loans.settings.SettingsScreenRouter
import com.panassevich.panassevich.feature.loans.tutorial.TutorialScreenRouter
import com.panassevich.panassevich.navigation.impl.LoanCreatedScreenRouterImpl
import com.panassevich.panassevich.navigation.impl.LoanDetailsScreenRouterImpl
import com.panassevich.panassevich.navigation.impl.LoansListScreenRouterImpl
import com.panassevich.panassevich.navigation.impl.LoginScreenRouterImpl
import com.panassevich.panassevich.navigation.impl.NewLoanScreenRouterImpl
import com.panassevich.panassevich.navigation.impl.RegistrationScreenRouterImpl
import com.panassevich.panassevich.navigation.impl.SettingsScreenRouterImpl
import com.panassevich.panassevich.navigation.impl.TutorialScreenRouterImpl
import dagger.Binds
import dagger.Module

@Module
interface RoutersModule {

    @Binds
    fun bindRegistrationScreenRouter(impl: RegistrationScreenRouterImpl): RegistrationScreenRouter

    @Binds
    fun bindLoginScreenRouter(impl: LoginScreenRouterImpl): LoginScreenRouter

    @Binds
    fun bindLoansListScreenRouter(impl: LoansListScreenRouterImpl): LoansListScreenRouter

    @Binds
    fun bindLoanDetailsScreenRouter(impl: LoanDetailsScreenRouterImpl): LoanDetailsScreenRouter

    @Binds
    fun bindSettingsScreenRouter(impl: SettingsScreenRouterImpl): SettingsScreenRouter

    @Binds
    fun bindTutorialScreenRouter(impl: TutorialScreenRouterImpl): TutorialScreenRouter

    @Binds
    fun bindNewLoanScreenRouter(impl: NewLoanScreenRouterImpl): NewLoanScreenRouter

    @Binds
    fun bindLoanCreatedScreenRouter(impl: LoanCreatedScreenRouterImpl): LoanCreatedScreenRouter
}