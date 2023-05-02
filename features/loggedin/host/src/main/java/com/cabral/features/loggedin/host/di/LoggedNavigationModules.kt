package com.cabral.features.loggedin.host.di

import com.cabral.core.LoggedNavigation
import com.cabral.core.NavigationFragment
import com.cabral.features.loggedin.host.navigation.LoggedNavigationImpl
import com.cabral.features.loggedin.host.navigation.NavigationFragmentImpl
import org.koin.core.module.Module
import org.koin.dsl.module

object LoggedNavigationModules {
    val modules get() = listOf(activityModules, fragmentModules)

    private val activityModules: Module = module {
        factory<LoggedNavigation> { LoggedNavigationImpl() }
    }

    private val fragmentModules: Module = module {
        factory<NavigationFragment> { NavigationFragmentImpl() }
    }
}