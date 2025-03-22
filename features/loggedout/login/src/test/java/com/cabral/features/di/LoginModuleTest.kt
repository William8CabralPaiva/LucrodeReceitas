package com.cabral.features.di

import com.cabral.core.common.domain.repository.UserRepository
import com.cabral.core.common.domain.usecase.ForgotPasswordUseCase
import com.cabral.core.common.domain.usecase.GoogleLoginUseCase
import com.cabral.core.common.domain.usecase.LoginUseCase
import com.cabral.features.presentation.LoginViewModel
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertNotNull


class LoginModuleTest : KoinTest {

    private val modules = LoginModule.modules

    private val mockUserRepository: UserRepository = mockk(relaxed = true)
    private val mockLoginUseCase: LoginUseCase = mockk(relaxed = true)
    private val mockGoogleLoginUseCase: GoogleLoginUseCase = mockk(relaxed = true)
    private val mockForgotPasswordUseCase: ForgotPasswordUseCase = mockk(relaxed = true)

    private val mockViewModelModules = module {
        factory { mockUserRepository }
        factory { mockLoginUseCase }
        factory { mockGoogleLoginUseCase }
        factory { mockForgotPasswordUseCase }
    }

    private val ingredientsViewModel: LoginViewModel by inject()

    @Before
    fun setUp() {
        startKoin {
            modules(
                modules + mockViewModelModules
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun verify_ingredientsViewModel_call() {
        assertNotNull(ingredientsViewModel)
    }
}
