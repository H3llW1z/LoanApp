package com.panassevich.panassevich

import android.app.Application
import com.panassevich.panassevich.di.DaggerLoanApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class LoanApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        DaggerLoanApplicationComponent.factory().create(this).inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> =
        dispatchingAndroidInjector
}