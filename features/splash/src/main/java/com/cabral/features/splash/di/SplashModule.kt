package com.cabral.features.splash.di

import com.cabral.features.splash.presentation.SplashScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object SplashModule {
    val modules get() = listOf(viewModelModules)

    private val viewModelModules: Module = module {
        viewModel {
            SplashScreenViewModel(get())
        }
    }
}