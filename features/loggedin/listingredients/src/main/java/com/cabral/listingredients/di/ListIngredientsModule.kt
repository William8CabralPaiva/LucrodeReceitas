package com.cabral.listingredients.di

import com.cabral.listingredients.presentation.ListIngredientsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object ListIngredientsModule {

    val modules get() = listOf(viewModelModules)

    private val viewModelModules: Module = module {
        viewModel {
            ListIngredientsViewModel(get())
        }
    }
}