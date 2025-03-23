package com.cabral.features.splash.presentation

import app.cash.turbine.test
import com.cabral.arch.extensions.UserThrowable
import com.cabral.core.common.SingletonUser
import com.cabral.core.common.domain.model.User
import com.cabral.core.common.domain.usecase.AutoLoginUseCase
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
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SplashScreenViewModelTest {

    private val autoLoginUseCase: AutoLoginUseCase = mockk(relaxed = true)
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

        // Act & Assert
        viewModel.uiEvent.test {
            viewModel.getUserLogged(key)
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert
            assertEquals(UiEvent.Logged, awaitItem())  // Expect Logged event to be emitted
            assertEquals(user, SingletonUser.getInstance().getUser())  // Ensure SingletonUser has the correct user
        }
    }

    @Test
    fun `getUserLogged with null key should emit Unlogged`() = runTest {
        // Arrange
        val key: String? = null

        // Act & Assert
        viewModel.uiEvent.test {
            viewModel.getUserLogged(key)
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert
            assertEquals(UiEvent.Unlogged, awaitItem())  // Expect Unlogged event to be emitted
        }
    }

    @Test
    fun `getUserLogged with invalid key should emit Unlogged`() = runTest {
        // Arrange
        val key = "invalidKey"
        coEvery { autoLoginUseCase(key) } returns flow { throw UserThrowable.UnknownUserThrowable() }

        // Act & Assert
        viewModel.uiEvent.test {
            viewModel.getUserLogged(key)
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert
            assertEquals(UiEvent.Unlogged, awaitItem())  // Expect Unlogged event to be emitted
        }
    }
}
