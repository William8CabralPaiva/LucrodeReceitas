package com.cabral.recipe.di

import com.cabral.recipe.presentation.ListRecipeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object ListRecipeModule {

    val modules get() = listOf(viewModelModules)

    private val viewModelModules: Module = module {
        viewModel {
            ListRecipeViewModel(get())
        }
    }
}