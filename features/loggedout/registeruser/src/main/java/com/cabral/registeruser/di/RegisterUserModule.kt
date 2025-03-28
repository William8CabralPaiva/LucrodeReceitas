package com.cabral.registeruser.di

import com.cabral.registeruser.presentation.RegisterUserViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

object RegisterUserModule {

    val modules get() = listOf(registerModules)

    private val registerModules: Module = module {
        viewModel { RegisterUserViewModel(get()) }
    }
}