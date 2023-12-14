package com.cabral.features.loggedin.host.di

import com.cabral.core.ListIngredientNavigation
import com.cabral.core.ListRecipeNavigation
import com.cabral.core.LoggedNavigation
import com.cabral.features.loggedin.host.di.navigation.ListIngredientNavigationImpl
import com.cabral.features.loggedin.host.di.navigation.ListRecipeNavigationImpl
import com.cabral.features.loggedin.host.di.navigation.LoggedNavigationImpl
import org.koin.core.module.Module
import org.koin.dsl.module

object LoggedNavigationModule {
    val modules get() = listOf(activityModules, fragmentModules)

    private val activityModules: Module = module {
        factory<LoggedNavigation> { LoggedNavigationImpl() }
    }

    private val fragmentModules: Module = module {
        factory<ListRecipeNavigation> { ListRecipeNavigationImpl() }
        factory<ListIngredientNavigation> { ListIngredientNavigationImpl() }
    }
}