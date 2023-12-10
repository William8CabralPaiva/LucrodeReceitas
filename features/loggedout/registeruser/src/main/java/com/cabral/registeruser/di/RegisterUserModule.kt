package com.cabral.registeruser.di

import com.cabral.registeruser.presentation.RegisterUserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object RegisterUserModule {

    val modules get() = listOf(registerModules)

    private val registerModules: Module = module {
        viewModel { RegisterUserViewModel(get()) }
    }
}