package com.cabral.hubsrc.repository

import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.RecipeProfitPrice
import com.cabral.remote.local.RemoteDataSource
import com.cabral.test_utils.stubs.recipeListStub
import com.cabral.test_utils.stubs.recipeProfitPriceListStub
import com.cabral.test_utils.stubs.recipeProfitPriceStub
import com.cabral.test_utils.stubs.recipeStub
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

import kotlin.test.assertEquals

class RecipeRepositoryImplTest {

    private val remoteDataSource: RemoteDataSource = mockk()
    private lateinit var recipeRepository: RecipeRepositoryImpl

    @Before
    fun setup() {
        recipeRepository = RecipeRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `getAllRecipes should return list of RecipeProfitPrice`() = runTest {
        // Arrange
        val mockResponse = recipeProfitPriceListStub()
        coEvery { remoteDataSource.getAllRecipes() } returns flowOf(mockResponse)

        // Act
        val result = recipeRepository.getAllRecipes()

        // Assert
        result.collect { recipes ->
            assertEquals(mockResponse, recipes)
        }
    }

    @Test
    fun `addRecipe should return success message`() = runTest {
        // Arrange
        val recipe = recipeStub()
        coEvery { remoteDataSource.addRecipe(recipe) } returns flowOf("Recipe added successfully")

        // Act
        val result = recipeRepository.addRecipe(recipe)

        // Assert
        result.collect { message ->
            assertEquals("Recipe added successfully", message)
        }
    }

    @Test
    fun `deleteRecipe should return unit`() = runTest {
        // Arrange
        val keyDocument = "1234"
        coEvery { remoteDataSource.deleteRecipe(keyDocument) } returns flowOf(Unit)

        // Act
        val result = recipeRepository.deleteRecipe(keyDocument)

        // Assert
        result.collect { response ->
            assertEquals(Unit, response)
        }
    }
}
