package com.panassevich.panassevich.di

import android.app.Application
import com.panassevich.panassevich.LoanApplication
import com.panassevich.panassevich.di.annotations.ApplicationScope
import com.panassevich.panassevich.di.module.DataModule
import com.panassevich.panassevich.di.module.NavigationModule
import com.panassevich.panassevich.di.module.RoutersModule
import com.panassevich.panassevich.di.module.ScreensModule
import com.panassevich.panassevich.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

@Component(
    modules = [DataModule::class,
        ViewModelModule::class,
        NavigationModule::class,
        RoutersModule::class,
        AndroidSupportInjectionModule::class,
        ScreensModule::class]
)
@ApplicationScope
interface LoanApplicationComponent {

    fun inject(app: LoanApplication)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): LoanApplicationComponent
    }

}