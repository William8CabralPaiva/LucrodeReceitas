package com.cabral.host.di

import com.cabral.core.NotLoggedNavigation
import com.cabral.host.navigation.NotLoggedNavigationImpl
import org.koin.core.module.Module
import org.koin.dsl.module

object NotLoggedNavigationModule {

    val modules get() = listOf(activityModules)

    private val activityModules: Module = module {
        factory<NotLoggedNavigation> { NotLoggedNavigationImpl() }
    }

}