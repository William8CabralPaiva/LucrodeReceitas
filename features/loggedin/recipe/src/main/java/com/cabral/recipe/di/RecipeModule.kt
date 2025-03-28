package com.cabral.recipe.di

import com.cabral.recipe.presentation.addeditrecipe.RecipeAddEditIngredientViewModel
import com.cabral.recipe.presentation.listrecipe.ListRecipeViewModel
import com.cabral.recipe.presentation.recipe.RecipeViewModel
import com.cabral.recipe.presentation.recipecosts.RecipeCostsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

object RecipeModule {

    val modules get() = listOf(viewModelModules)

    private val viewModelModules: Module = module {
        viewModel { RecipeAddEditIngredientViewModel(get()) }
        viewModel { ListRecipeViewModel(get(), get()) }
        viewModel { RecipeViewModel(get()) }
        viewModel { RecipeCostsViewModel(get()) }
    }
}