package com.cabral.recipe.presentation.addeditrecipe

import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.usecase.ListIngredientUseCase
import com.cabral.test_utils.stubs.ingredientListStub
import com.cabral.test_utils.stubs.ingredientStub
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeAddEditIngredientViewModelTest {

    private val listIngredientUseCase: ListIngredientUseCase = mockk(relaxed = true)
    private lateinit var viewModel: RecipeAddEditIngredientViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RecipeAddEditIngredientViewModel(listIngredientUseCase)
        viewModel.recipe = Recipe(id = 1, name = "Recipe Test")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAllIngredients should emit ListIngredient when use case returns data`() = runTest {
        val ingredients = ingredientListStub()
        coEvery { listIngredientUseCase() } returns flowOf(ingredients)
        val states = mutableListOf<UiState>()
        val collectJob = launch { viewModel.uiState.collect { states.add(it) } }

        viewModel.getAllIngredients()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(states.any { it is UiState.ListIngredient })
        collectJob.cancel()
    }

    @Test
    fun `getAllIngredients should emit Error when use case throws an exception`() = runTest {
        coEvery { listIngredientUseCase() } returns flow { throw Exception("Error") }
        val states = mutableListOf<UiState>()
        val collectJob = launch { viewModel.uiState.collect { states.add(it) } }

        viewModel.getAllIngredients()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(states.any { it is UiState.Error })
        collectJob.cancel()
    }

    @Test
    fun `addIngredientInList with invalid ingredient name should emit ErrorSpinner`() = runTest {
        val states = mutableListOf<UiState>()
        val collectJob = launch { viewModel.uiState.collect { states.add(it) } }

        viewModel.addIngredientInList("Invalid Ingredient", 20.0f)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(states.any { it is UiState.ErrorSpinner })
        collectJob.cancel()
    }

    @Test
    fun `deleteItemAdd should remove ingredient and emit RemoveIngredient`() = runTest {
        val ingredient = ingredientStub()
        viewModel.listAddIngredients.add(ingredient)
        val states = mutableListOf<UiState>()
        val collectJob = launch { viewModel.uiState.collect { states.add(it) } }

        viewModel.deleteItemAdd(ingredient)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(states.any { it is UiState.RemoveIngredient })
        assertEquals(0, viewModel.listAddIngredients.size)
        collectJob.cancel()
    }

    @Test
    fun `setEditMode should update state and set correct edit position`() = runTest {
        val ingredient = ingredientStub()
        viewModel.listAddIngredients.add(ingredient)
        val states = mutableListOf<UiState>()
        val collectJob = launch { viewModel.uiState.collect { states.add(it) } }

        viewModel.setEditMode(true, ingredient)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(states.any { it is UiState.EditMode && it.editMode })
        collectJob.cancel()
    }

    @Test
    fun `addIngredientInList should edit existing ingredient when in edit mode`() = runTest {
        val ingredient = ingredientStub()
        viewModel.listAddIngredients.add(ingredient)
        viewModel.setEditMode(true, ingredient)
        val states = mutableListOf<UiState>()
        val collectJob = launch { viewModel.uiState.collect { states.add(it) } }

        viewModel.addIngredientInList("Edited Ingredient", 50.0f)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(states.any { it is UiState.SuccessEdit })
        assertEquals("Edited Ingredient", viewModel.listAddIngredients[0]?.name)
        collectJob.cancel()
    }
}
