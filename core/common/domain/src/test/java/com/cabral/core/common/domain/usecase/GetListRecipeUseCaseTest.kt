package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.repository.RecipeRepository
import com.cabral.test_utils.stubs.recipeProfitPriceListStub
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertFailsWith


@ExperimentalCoroutinesApi
class GetListRecipeUseCaseTest {

    private val repository: RecipeRepository = mockk()
    private val subject = GetListRecipeUseCase(repository)

    @Test
    fun `getAllRecipes Should return recipe list`() = runTest {
        //Arrange
        val expectedResult = flowOf(recipeProfitPriceListStub())
        coEvery { repository.getAllRecipes() } returns expectedResult

        //Act
        val result = subject()

        //Assert
        assertEquals(expectedResult, result)
        coVerify { repository.getAllRecipes() }

    }

    @Test
    fun `getAllRecipes Should return recipe failure`() = runTest {
        //Arrange
        val expectedResult = "Test error"
        val throwable = Throwable(expectedResult)
        coEvery { repository.getAllRecipes() } throws throwable

        //Act
        val result = assertFailsWith<Throwable> { subject() }

        //Assert
        assertEquals(expectedResult, result.message)
        coVerify { repository.getAllRecipes() }

    }

}