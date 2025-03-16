package com.cabral.features.splash.presentation

import com.cabral.core.common.SingletonUser
import com.cabral.core.common.domain.model.User
import com.cabral.core.common.domain.usecase.AutoLoginUseCase
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
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashScreenViewModelTest {

    private val autoLoginUseCase: AutoLoginUseCase = mockk()
    private lateinit var viewModel: SplashScreenViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SplashScreenViewModel(autoLoginUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getUserLogged with valid key should emit Logged`() = runTest {
        // Arrange
        val key = "validKey"
        val user = User("test@example.com", "Test User", "Password123!")
        coEvery { autoLoginUseCase(key) } returns flowOf(user)

        val events = mutableListOf<UiEvent>()
        val collectJob = launch {
            viewModel.uiEvent.collect { events.add(it) }
        }

        // Act
        viewModel.getUserLogged(key)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(listOf(UiEvent.Logged), events)
        assertEquals(user, SingletonUser.getInstance().getUser())
        collectJob.cancel()
    }

    @Test
    fun `getUserLogged with null key should emit Unlogged`() = runTest {
        // Arrange
        val key: String? = null

        val events = mutableListOf<UiEvent>()
        val collectJob = launch {
            viewModel.uiEvent.collect { events.add(it) }
        }

        // Act
        viewModel.getUserLogged(key)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(listOf(UiEvent.Unlogged), events)
        collectJob.cancel()
    }

    @Test
    fun `getUserLogged with invalid key should emit Unlogged`() = runTest {
        // Arrange
        val key = "invalidKey"
        coEvery { autoLoginUseCase(key) } returns flow { throw Exception("User not found") }

        val events = mutableListOf<UiEvent>()
        val collectJob = launch {
            viewModel.uiEvent.collect { events.add(it) }
        }

        // Act
        viewModel.getUserLogged(key)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(listOf(UiEvent.Unlogged), events)
        collectJob.cancel()
    }
}