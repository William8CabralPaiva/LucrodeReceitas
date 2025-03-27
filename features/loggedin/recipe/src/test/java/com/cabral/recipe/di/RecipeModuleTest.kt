package com.cabral.recipe.di

import com.cabral.core.common.domain.usecase.AddRecipeUseCase
import com.cabral.core.common.domain.usecase.CostRecipeUseCase
import com.cabral.core.common.domain.usecase.DeleteRecipeUseCase
import com.cabral.core.common.domain.usecase.GetListRecipeUseCase
import com.cabral.core.common.domain.usecase.ListIngredientUseCase
import com.cabral.recipe.presentation.addeditrecipe.RecipeAddEditIngredientViewModel
import com.cabral.recipe.presentation.listrecipe.ListRecipeViewModel
import com.cabral.recipe.presentation.recipe.RecipeViewModel
import com.cabral.recipe.presentation.recipecosts.RecipeCostsViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeModuleTest : KoinTest {

    private val testDispatcher = StandardTestDispatcher()

    private val modules = RecipeModule.modules

    private val mockListIngredientUseCase: ListIngredientUseCase = mockk(relaxed = true)
    private val mockGetListRecipeUseCase: GetListRecipeUseCase = mockk(relaxed = true)
    private val mockDeleteRecipeUseCase: DeleteRecipeUseCase = mockk(relaxed = true)
    private val mockAddRecipeUseCase: AddRecipeUseCase = mockk(relaxed = true)
    private val mockCostRecipeUseCase: CostRecipeUseCase = mockk(relaxed = true)


    private val mockViewModelModules = module {
        factory { mockListIngredientUseCase }
        factory { mockGetListRecipeUseCase }
        factory { mockDeleteRecipeUseCase }
        factory { mockAddRecipeUseCase }
        factory { mockCostRecipeUseCase }
    }

    private val recipeAddEditIngredientViewModel: RecipeAddEditIngredientViewModel by inject()
    private val listRecipeViewModel: ListRecipeViewModel by inject()
    private val recipeViewModel: RecipeViewModel by inject()
    private val recipeCostsViewModel: RecipeCostsViewModel by inject()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        startKoin {
            modules(
                modules + mockViewModelModules
            )
        }
    }

    @After
    fun tearDown() {
        Dispatchers.setMain(testDispatcher)
        stopKoin()
    }

    @Test
    fun verifyRecipeAddEditIngredientViewModelCall() {
        assertNotNull(recipeAddEditIngredientViewModel)
    }

    @Test
    fun verifyListRecipeViewModelCall() {
        assertNotNull(listRecipeViewModel)
    }

    @Test
    fun verifyRecipeViewModelCall() {
        assertNotNull(recipeViewModel)
    }

    @Test
    fun verifyRecipeCostsViewModelCall() {
        assertNotNull(recipeCostsViewModel)
    }
}