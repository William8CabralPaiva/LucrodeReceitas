package com.cabral.lucrodereceitas.di

import com.cabral.core.NavigationActivity
import com.cabral.core.NavigationFragment
import com.cabral.lucrodereceitas.NavigationActivityImpl
import com.cabral.lucrodereceitas.NavigationFragmentImpl
import org.koin.core.module.Module
import org.koin.dsl.module

object NavigationModules {

    val modules get() = listOf(activityModules,fragmentModules)

    private val activityModules: Module = module {
        factory<NavigationActivity> { NavigationActivityImpl() }
    }

    private val fragmentModules: Module = module {
        factory<NavigationFragment> { NavigationFragmentImpl() }
    }
}