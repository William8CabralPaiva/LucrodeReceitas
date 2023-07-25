package com.cabral.features.di

import com.cabral.core.common.domain.usecase.AddUserUseCase
import com.cabral.features.presentation.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object LoginModules {
    val modules get() = listOf(viewModelModules)

    private val viewModelModules: Module = module {
        viewModel { LoginViewModel(addUserUseCase = AddUserUseCase(get())) }
    }
}