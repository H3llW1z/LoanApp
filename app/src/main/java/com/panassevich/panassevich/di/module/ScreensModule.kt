package com.panassevich.panassevich.di.module

import com.panassevich.panassevich.feature.loans.loancreated.ui.LoanCreatedFragment
import com.panassevich.panassevich.feature.loans.loandetails.ui.LoanDetailsFragment
import com.panassevich.panassevich.feature.loans.loanslist.ui.LoansListFragment
import com.panassevich.panassevich.feature.loans.login.ui.LoginFragment
import com.panassevich.panassevich.feature.loans.newloan.ui.NewLoanFragment
import com.panassevich.panassevich.feature.loans.registration.ui.RegistrationFragment
import com.panassevich.panassevich.feature.loans.settings.ui.SettingsFragment
import com.panassevich.panassevich.feature.loans.tutorial.ui.TutorialFragment
import com.panassevich.panassevich.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ScreensModule {

    @ContributesAndroidInjector
    fun contributesMainActivityInjector(): MainActivity

    @ContributesAndroidInjector
    fun contributesRegistrationFragmentInjector(): RegistrationFragment

    @ContributesAndroidInjector
    fun contributesLoginFragmentInjector(): LoginFragment

    @ContributesAndroidInjector
    fun contributesLoansListFragmentInjector(): LoansListFragment

    @ContributesAndroidInjector
    fun contributeLoanDetailsFragmentInjector(): LoanDetailsFragment

    @ContributesAndroidInjector
    fun contributesSettingsFragmentInjector(): SettingsFragment

    @ContributesAndroidInjector
    fun contributeTutorialFragmentInjector(): TutorialFragment

    @ContributesAndroidInjector
    fun contributeNewLoanFragmentInjector(): NewLoanFragment

    @ContributesAndroidInjector
    fun contributeLoanCreatedFragmentInjector(): LoanCreatedFragment
}