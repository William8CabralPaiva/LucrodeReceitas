package com.cabral.features.di

import com.cabral.core.common.domain.usecase.ForgotPasswordUseCase
import com.cabral.core.common.domain.usecase.GoogleLoginUseCase
import com.cabral.core.common.domain.usecase.LoginUseCase
import com.cabral.features.presentation.LoginViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

object LoginModule {
    val modules get() = listOf(viewModelModules)

    private val viewModelModules: Module = module {
        viewModel {
            LoginViewModel(
                loginUseCase = LoginUseCase(get()),
                googleLoginUseCase = GoogleLoginUseCase(get()),
                forgotPasswordUseCase = ForgotPasswordUseCase(get())
            )
        }
    }
}