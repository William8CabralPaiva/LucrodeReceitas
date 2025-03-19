package com.cabral.features.presentation

import app.cash.turbine.test
import com.cabral.core.common.domain.usecase.ForgotPasswordUseCase
import com.cabral.core.common.domain.usecase.GoogleLoginUseCase
import com.cabral.core.common.domain.usecase.LoginUseCase
import com.cabral.test_utils.stubs.userStub
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private val loginUseCase: LoginUseCase = mockk(relaxed = true)
    private val googleLoginUseCase: GoogleLoginUseCase = mockk(relaxed = true)
    private val forgotPasswordUseCase: ForgotPasswordUseCase = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(loginUseCase, googleLoginUseCase, forgotPasswordUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test login triggers correct uiEvent flow`() = runTest {
        // Arrange
        val user = userStub()
        coEvery { loginUseCase(any()) } returns flowOf(user)

        // Act & Assert
        viewModel.uiEvent.test {
            viewModel.login(user.email, user.password)

            assertEquals(UiState.StartLoading, awaitItem())
            assertEquals(UiState.Success(user), awaitItem())
            assertEquals(UiState.StopLoading, awaitItem())
        }
    }

    @Test
    fun `test login handles error state`() = runTest {
        // Arrange
        val user = userStub()
        coEvery { loginUseCase(any()) } returns flow { throw Exception("Login failed") }

        // Act & Assert
        viewModel.uiEvent.test {
            viewModel.login(user.email, user.password)

            assertEquals(UiState.StartLoading, awaitItem())
            assertEquals(UiState.Error("Login failed"), awaitItem())
            assertEquals(UiState.StopLoading, awaitItem())
        }
    }

    @Test
    fun `test googleEmail triggers correct uiEvent flow`() = runTest {
        // Arrange
        val user = userStub()
        coEvery { googleLoginUseCase(user.email!!, "Google User") } returns flowOf(user)

        // Act & Assert
        viewModel.uiEvent.test {
            viewModel.googleEmail(user.email, "Google User")

            assertEquals(UiState.GoogleStartLoading, awaitItem())
            assertEquals(UiState.Success(user), awaitItem())
            assertEquals(UiState.GoogleStopLoading, awaitItem())
        }
    }

    @Test
    fun `test forgotPassword with valid email triggers correct uiEvent`() = runTest {
        // Arrange
        val email = "test@example.com"
        coEvery { forgotPasswordUseCase(email) } returns Unit

        // Act & Assert
        viewModel.uiEvent.test {
            viewModel.forgotPassword(email)

            assertEquals(UiState.ForgotPassword, awaitItem())
        }
    }
}
