package com.panassevich.panassevich.di.module

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.panassevich.panassevich.di.annotations.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class NavigationModule {

    @Provides
    @ApplicationScope
    fun provideCicerone(): Cicerone<Router> {
        return Cicerone.create()
    }

    @Provides
    @ApplicationScope
    fun provideRouter(cicerone: Cicerone<Router>): Router {
        return cicerone.router
    }

    @Provides
    @ApplicationScope
    fun provideNavigatorHolder(cicerone: Cicerone<Router>): NavigatorHolder {
        return cicerone.getNavigatorHolder()
    }
}