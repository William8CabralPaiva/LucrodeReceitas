package com.cabral.ingredient.di

import com.cabral.ingredient.presentation.listingredient.ListIngredientsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object ListIngredientsModule {

    val modules get() = listOf(viewModelModules)

    private val viewModelModules: Module = module {
        viewModel {
            ListIngredientsViewModel(get(),get())
        }
    }
}