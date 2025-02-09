package com.cabral.recipe.presentation.recipe

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.cabral.core.common.domain.usecase.AddRecipeUseCase
import com.cabral.testing.DispatcherTestRule
import com.cabral.testing.stubs.recipeStub
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RecipeViewModelTest {
    @get:Rule
    val executorRule = InstantTaskExecutorRule()

    @get:Rule
    val dispatcherRule = DispatcherTestRule()

    private val useCase: AddRecipeUseCase = mockk()

    private lateinit var subject: RecipeViewModel

    private val recipe = recipeStub()


    @Before
    fun setUp() {
        subject = RecipeViewModel(
            useCase
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }


    @Test
    fun `addRecipe should emit Error when Throwable ocurrs`() = runTest {
        // Arrange
        val errorMessage = "An error occurred"
        val throwable = Throwable(errorMessage)
        coEvery { useCase(recipe) } returns flow { throw throwable }

        // Act

        subject.addRecipe(recipe.name, recipe.volume, recipe.expectedProfit)

        // Assert
        subject.uiEvent.test {
            assertEquals(UiEvent.StartLoading, awaitItem())

            val errorEvent = awaitItem() as UiEvent.Error
            assertEquals(errorMessage, errorEvent.message)

            assertEquals(UiEvent.StopLoading, awaitItem())
        }


        coVerify { useCase(recipe) }
    }

    @Test
    fun `addRecipe should emit Success when recipe is created successfully`() = runTest {
        //Arrange
        coEvery { useCase(recipe) } returns flowOf(recipe.keyDocument)

        //Act
        subject.addRecipe(recipe.name, recipe.volume, recipe.expectedProfit)

        //Assert
        subject.uiEvent.test {
            assertEquals(UiEvent.StartLoading, awaitItem())
            assertEquals(UiEvent.Success, awaitItem())
            assertEquals(UiEvent.StopLoading, awaitItem())
        }

        coVerify { subject.addRecipe(recipe.name, recipe.volume, recipe.expectedProfit) }

    }

}

