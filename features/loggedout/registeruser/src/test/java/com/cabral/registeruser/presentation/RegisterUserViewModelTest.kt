package com.cabral.registeruser.presentation

import app.cash.turbine.test
import com.cabral.core.common.domain.usecase.AddUserUseCase
import com.cabral.test_utils.stubs.stubNetWorkException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
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
class RegisterUserViewModelTest {

    private val addUserUseCase: AddUserUseCase = mockk()
    private lateinit var viewModel: RegisterUserViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RegisterUserViewModel(addUserUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `registerUser with valid data should emit StartLoading and Success`() = runTest {
        // Arrange
        val name = "Test User"
        val email = "test@example.com"
        val password = "Password123!"
        val confirmPassword = "Password123!"

        coEvery { addUserUseCase(any()) } returns flowOf(Unit)

        // Act & Assert
        viewModel.uiEvent.test {
            viewModel.registerUser(name, email, password, confirmPassword)
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert the emitted events
            assertEquals(UiEvent.StartLoading, awaitItem())  // StartLoading event
            assertEquals(UiEvent.Success, awaitItem())      // Success event
            cancelAndIgnoreRemainingEvents()  // Ensure no more events are emitted
        }
    }

    @Test
    fun `registerUser with invalid name should emit ErrorUsername`() = runTest {
        // Arrange
        val name = "T"  // Invalid name (too short)
        val email = "test@example.com"
        val password = "Password123!"
        val confirmPassword = "Password123!"

        // Act & Assert
        viewModel.uiState.test {
            viewModel.registerUser(name, email, password, confirmPassword)
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert the emitted state
            assertTrue(awaitItem() is UiState.DefaultFieldsState)
            assertTrue(awaitItem() is UiState.ErrorUsername)  // ErrorUsername state
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `registerUser with invalid email should emit ErrorEmail`() = runTest {
        // Arrange
        val name = "Test User"
        val email = "invalid_email"  // Invalid email format
        val password = "Password123!"
        val confirmPassword = "Password123!"

        // Act & Assert
        viewModel.uiState.test {
            viewModel.registerUser(name, email, password, confirmPassword)
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert the emitted state
            assertTrue(awaitItem() is UiState.DefaultFieldsState)
            assertTrue(awaitItem() is UiState.ErrorEmail)  // ErrorEmail state
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `registerUser with invalid password should emit ErrorPassword`() = runTest {
        // Arrange
        val name = "Test User"
        val email = "test@example.com"
        val password = "short"  // Invalid password (too short)
        val confirmPassword = "short"

        // Act & Assert
        viewModel.uiState.test {
            viewModel.registerUser(name, email, password, confirmPassword)
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert the emitted state
            assertTrue(awaitItem() is UiState.DefaultFieldsState)
            assertTrue(awaitItem() is UiState.ErrorPassword)  // ErrorPassword state
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `registerUser with different passwords should emit ErrorConfirmPassword`() = runTest {
        // Arrange
        val name = "Test User"
        val email = "test@example.com"
        val password = "Password123!"
        val confirmPassword = "DifferentPassword123!"

        // Act & Assert
        viewModel.uiState.test {
            viewModel.registerUser(name, email, password, confirmPassword)
            testDispatcher.scheduler.advanceUntilIdle()


            assertTrue(awaitItem() is UiState.DefaultFieldsState)
            assertTrue(awaitItem() is UiState.ErrorConfirmPassword)  // ErrorConfirmPassword state
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `registerUser with error in addUserUseCase should emit Error`() = runTest {
        // Arrange
        val name = "Test User"
        val email = "test@example.com"
        val password = "Password123!"
        val confirmPassword = "Password123!"
        val stubException = stubNetWorkException()

        coEvery { addUserUseCase(any()) } returns flow { throw stubException }

        // Act & Assert
        viewModel.uiEvent.test {
            viewModel.registerUser(name, email, password, confirmPassword)
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert the emitted events
            assertEquals(UiEvent.StartLoading, awaitItem())  // StartLoading event
            assertEquals(UiEvent.Error, awaitItem())        // Error event
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `setDefaultFieldsState should emit DefaultFieldsState`() = runTest {
        // Act & Assert
        viewModel.uiState.test {
            viewModel.setDefaultFieldsState()
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert the emitted state
            assertEquals(UiState.DefaultFieldsState, awaitItem())  // DefaultFieldsState
            cancelAndIgnoreRemainingEvents()
        }
    }
}
