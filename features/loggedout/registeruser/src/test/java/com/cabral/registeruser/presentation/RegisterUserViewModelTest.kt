package com.cabral.registeruser.presentation

import com.cabral.core.common.domain.usecase.AddUserUseCase
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
    fun `registerUser with valid data should emit Success and StartLoading`() = runTest {
        // Arrange
        val name = "Test User"
        val email = "test@example.com"
        val password = "Password123!"
        val confirmPassword = "Password123!"

        coEvery { addUserUseCase(any()) } returns flowOf(Unit)

        val events = mutableListOf<UiEvent>()
        val collectJob = launch { // collectJob individual
            viewModel.uiEvent.collect { events.add(it) }
        }

        // Act
        viewModel.registerUser(name, email, password, confirmPassword)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(listOf(UiEvent.StartLoading, UiEvent.Success), events)
        collectJob.cancel()
    }

    @Test
    fun `registerUser with invalid name should emit ErrorUsername`() = runTest {
        // Arrange
        val name = "T"
        val email = "test@example.com"
        val password = "Password123!"
        val confirmPassword = "Password123!"

        val states = mutableListOf<UiState>()
        val collectJob = launch { // collectJob individual
            viewModel.uiState.collect { states.add(it) }
        }

        // Act
        viewModel.registerUser(name, email, password, confirmPassword)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertTrue(states.any { it is UiState.ErrorUsername })
        collectJob.cancel()
    }

    @Test
    fun `registerUser with invalid email should emit ErrorEmail`() = runTest {
        // Arrange
        val name = "Test User"
        val email = "invalid_email"
        val password = "Password123!"
        val confirmPassword = "Password123!"

        val states = mutableListOf<UiState>()
        val collectJob = launch { // collectJob individual
            viewModel.uiState.collect { states.add(it) }
        }

        // Act
        viewModel.registerUser(name, email, password, confirmPassword)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertTrue(states.any { it is UiState.ErrorEmail })
        collectJob.cancel()
    }

    @Test
    fun `registerUser with invalid password should emit ErrorPassword`() = runTest {
        // Arrange
        val name = "Test User"
        val email = "test@example.com"
        val password = "password"
        val confirmPassword = "password"

        val states = mutableListOf<UiState>()
        val collectJob = launch { // collectJob individual
            viewModel.uiState.collect { states.add(it) }
        }

        // Act
        viewModel.registerUser(name, email, password, confirmPassword)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertTrue(states.any { it is UiState.ErrorPassword })
        collectJob.cancel()
    }

    @Test
    fun `registerUser with different passwords should emit ErrorConfirmPassword`() = runTest {
        // Arrange
        val name = "Test User"
        val email = "test@example.com"
        val password = "Password123!"
        val confirmPassword = "DifferentPassword!"

        val states = mutableListOf<UiState>()
        val collectJob = launch { // collectJob individual
            viewModel.uiState.collect { states.add(it) }
        }

        // Act
        viewModel.registerUser(name, email, password, confirmPassword)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertTrue(states.any { it is UiState.ErrorConfirmPassword })
        collectJob.cancel()
    }

    @Test
    fun `registerUser with error in addUserUseCase should emit Error`() = runTest {
        // Arrange
        val name = "Test User"
        val email = "test@example.com"
        val password = "Password123!"
        val confirmPassword = "Password123!"
        val errorMessage = "UseCase Error"

        coEvery { addUserUseCase(any()) } returns flow { throw Exception(errorMessage) }

        val events = mutableListOf<UiEvent>()
        val collectJob = launch { // collectJob individual
            viewModel.uiEvent.collect { events.add(it) }
        }

        // Act
        viewModel.registerUser(name, email, password, confirmPassword)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(listOf(UiEvent.StartLoading, UiEvent.Error), events)
        collectJob.cancel()
    }

    @Test
    fun `setDefaultFieldsState should emit DefaultFieldsState`() = runTest {
        // Arrange
        val states = mutableListOf<UiState>()
        val collectJob = launch { // collectJob individual
            viewModel.uiState.collect { states.add(it) }
        }

        // Act
        viewModel.setDefaultFieldsState()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(UiState.DefaultFieldsState, states.last())
        collectJob.cancel()
    }
}