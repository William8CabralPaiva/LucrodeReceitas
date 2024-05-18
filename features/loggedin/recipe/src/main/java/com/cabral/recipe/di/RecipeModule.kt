package com.cabral.recipe.di

import com.cabral.recipe.presentation.ListRecipeViewModel
import com.cabral.recipe.presentation.RecipeAddEditIngredientFragmentViewModel
import com.cabral.recipe.presentation.RecipeCostsViewModel
import com.cabral.recipe.presentation.RecipeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object RecipeModule {

    val modules get() = listOf(viewModelModules)

    private val viewModelModules: Module = module {
        viewModel {
            RecipeAddEditIngredientFragmentViewModel(get())
        }
        viewModel { ListRecipeViewModel(get(),get()) }
        viewModel { RecipeViewModel(get()) }
        viewModel { RecipeCostsViewModel(get()) }
    }
}