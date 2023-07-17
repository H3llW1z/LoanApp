package com.panassevich.panassevich.di.module

import androidx.lifecycle.ViewModel
import com.panassevich.panassevich.di.annotations.ViewModelKey
import com.panassevich.panassevich.feature.loans.loancreated.presentation.LoanCreatedViewModel
import com.panassevich.panassevich.feature.loans.loandetails.presentation.LoanDetailsViewModel
import com.panassevich.panassevich.feature.loans.loanslist.presentation.LoansListViewModel
import com.panassevich.panassevich.feature.loans.login.presentation.LoginViewModel
import com.panassevich.panassevich.feature.loans.newloan.presentation.NewLoanViewModel
import com.panassevich.panassevich.feature.loans.registration.presentation.RegistrationViewModel
import com.panassevich.panassevich.feature.loans.settings.presentation.SettingsViewModel
import com.panassevich.panassevich.feature.loans.tutorial.presentation.TutorialViewModel
import com.panassevich.panassevich.presentation.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegistrationViewModel::class)
    fun bindRegistrationViewModel(viewModel: RegistrationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TutorialViewModel::class)
    fun bindTutorialViewModel(viewModel: TutorialViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoansListViewModel::class)
    fun bindLoansListViewModel(viewModel: LoansListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoanDetailsViewModel::class)
    fun bindLoanDetailsViewModel(viewModel: LoanDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewLoanViewModel::class)
    fun bindNewLoanViewModel(viewModel: NewLoanViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoanCreatedViewModel::class)
    fun bindLoanCreatedViewModel(viewModel: LoanCreatedViewModel): ViewModel
}